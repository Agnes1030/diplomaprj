package com.febs.shop.service.impl;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.febs.common.domain.ResponseBo;
import com.febs.shop.domain.CartItem;
import com.febs.shop.domain.ProductSku;
import com.febs.shop.domain.ShoppingCart;
import com.febs.shop.service.ProductSkuService;
import com.febs.shop.service.ShoppingCartService;
import com.febs.system.domain.MyUser;
import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tk.mybatis.mapper.entity.Example;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductSkuService productSkuService;

	@Autowired
	JedisPool jedisPool;

	/**
	 * 获得用户key
	 *
	 * 1.用户未登录情况下第一次进入购物车 -> 生成key 保存至cookie中 2.用户未登录情况下第n进入购物车 -> 从cookie中取出key
	 * 3.用户登录情况下 -> 根据用户code生成key 4.用户登录情况下并且cookie中存在key-> 从cookie取的的key从缓存取得购物车
	 * 合并至 用户code生成key的购物车中去 ，这样后面才能根据用户code 取得正确的购物车
	 *
	 * @param req
	 * @param resp
	 * @param account
	 * @return
	 */
	@Override
	public String getKey(HttpServletRequest req, HttpServletResponse resp, MyUser myUser) {
		String key = null; // 最终返回的key
		String tempKey = ""; // 用来存储cookie中的临时key,

		Cookie cartCookie = WebUtils.getCookie(req, "shoopingCart");
		if (cartCookie != null) {
			// 获取Cookie中的key
			key = cartCookie.getValue();
			tempKey = cartCookie.getValue();
		}
		if (StringUtils.isBlank(key)) {
			key = ShoppingCart.unLoginKeyPrefix + UUID.randomUUID();
			if (myUser != null)
				key = ShoppingCart.loginKeyPrefix + myUser.getUsername();
			Cookie cookie = new Cookie("shoopingCart", key);
			cookie.setMaxAge(-1);
			cookie.setPath("/");
			resp.addCookie(cookie);
		} else if (StringUtils.isNotBlank(key) && myUser != null) {// ⑵
			key = ShoppingCart.loginKeyPrefix + myUser.getUsername();
			if (tempKey.startsWith(ShoppingCart.unLoginKeyPrefix)) {// ⑴
				// 1.满足cookie中取得的key 为未登录时的key
				// 2.满足当前用户已经登录
				// 3.合并未登录时用户所添加的购物车商品⑷
				mergeCart(tempKey, myUser);// ⑶
			}
		}
		return key;
	}

	@Override
	public ShoppingCart mergeCart(String tempKey, MyUser myUser) {
		ShoppingCart loginCart = null;
		String loginkey = null;

		// 从redis取出用户缓存购物车数据
		Jedis jedis = jedisPool.getResource();
		Gson gson = new Gson();
		String unLoginCartJson = jedis.hget("CACHE_SHOPPINGCART", tempKey);
		ShoppingCart unLoginCart;
		if (unLoginCartJson == null) {
			unLoginCart = new ShoppingCart(tempKey);
		} else {
			unLoginCart = gson.fromJson(unLoginCartJson, ShoppingCart.class);
		}
		if (myUser != null && tempKey.startsWith(ShoppingCart.unLoginKeyPrefix)) {// ⑵
			// 如果用户登录 并且 当前是未登录的key
			loginkey = ShoppingCart.loginKeyPrefix + myUser.getUsername();
			loginCart = mergeCart(loginkey, myUser);
			if (null != unLoginCart.getCartItems()) {// ⑴

				if (null != loginCart.getCartItems()) {
					// 满足未登录时的购物车不为空 并且 当前用户已经登录
					// 进行购物车合并
					for (CartItem cv : unLoginCart.getCartItems()) {
						long count = loginCart.getCartItems().stream()
								.filter(it -> it.getSpecificationIds().equals(cv.getSpecificationIds())).count();
						if (count == 0) {// 没有重复的商品 则直接将商品加入购物车
							loginCart.getCartItems().add(cv);
						} else if (count == 1) {// 出现重复商品 修改数量
							CartItem c = loginCart.getCartItems().stream()
									.filter(it -> it.getSpecificationIds().equals(cv.getSpecificationIds()))
									.findFirst().orElse(null);
							c.setQuantity(c.getQuantity() + 1);
						}
					}
				} else {
					// 如果当前登录用户的购物车为空则 将未登录时的购物车合并
					loginCart.setCartItems(unLoginCart.getCartItems());
				}
				unLoginCart = loginCart;
				// 【删除临时key】
				jedis.hdel("CACHE_SHOPPINGCART", tempKey);
				// 【将合并后的购物车数据 放入loginKey】//TMP_4369f86d-c026-4b1b-8fec-f3c69f6ffac5
				jedis.hset("CACHE_SHOPPINGCART", loginkey, gson.toJson(unLoginCart));
				
			}
		}
		jedis.close();
		return unLoginCart;
	}

	/**
	 * 添加购物车
	 * 
	 * @param req
	 * @param resp
	 * @param myUser 登陆用户信息
	 * @param item   添加的购物车商品信息 包含商品code 商品加购数量
	 * @return
	 */
	@Override
	public ResponseBo addCart(HttpServletRequest req, HttpServletResponse resp, MyUser myUser, CartItem item) {
		String key = getKey(req, resp, myUser);// 得到最终key
		ShoppingCart cacheCart = mergeCart(key, myUser);// 根据key取得最终购物车对象
		if (null != item.getProductId() && item.getQuantity() > 0) {
			// TODO 进行一系列 商品上架 商品code是否正确 最大购买数量....
			// 根据规格参数组合（例如:尺寸:XL,颜色:白色）查询SKU信息
			String specificationIds = item.getSpecificationIds();
			Example example=new Example(ProductSku.class);
			example.createCriteria().andEqualTo("productId",item.getProductId()).andEqualTo("specificationIds",specificationIds);
			List<ProductSku> productSkus= productSkuService.selectByExample(example);
			if(null==productSkus ||productSkus.size()==0) {
				return ResponseBo.error("商品不存在");
			}
			ProductSku productSku = productSkus.get(0);
			if (productSku.getStock()<=0 || item.getQuantity()>productSku.getStock()) {
				return ResponseBo.error("库存不足");
			}
			long count = 0;
			if (null != cacheCart.getCartItems()) {
				count = cacheCart.getCartItems().stream()
						.filter(it -> it.getSpecificationIds().equals(item.getSpecificationIds())).count();
			}
			if (count == 0) {
				item.setSpecificationIds(productSku.getSpecificationIds());
				// 之前购物车无该商品记录 则直接添加
				cacheCart.getCartItems().add(item);
			} else {
				// 否则将同一商品数量相加
				CartItem c = cacheCart.getCartItems().stream()
						.filter(it -> it.getSpecificationIds().equals(item.getSpecificationIds())).findFirst()
						.orElse(null);
				c.setQuantity(c.getQuantity() + item.getQuantity());

			}
		}
		// 【将合并后的购物车数据 放入loginKey】
		Jedis jedis = jedisPool.getResource();
		Gson gson = new Gson();
		jedis.hset("CACHE_SHOPPINGCART", key, gson.toJson(cacheCart));
		jedis.close();
		return ResponseBo.ok(cacheCart);
	}
    /**
     * 清空购物车 
     */
	public ResponseBo clearCart(HttpServletRequest req,HttpServletResponse resp, MyUser myUser) {
		String key = getKey(req, resp, myUser);// 得到最终key
		Jedis jedis = jedisPool.getResource();
		jedis.hdel("CACHE_SHOPPINGCART", key);
		// 删除cookie
		Cookie cookie = new Cookie("shoopingCart", key);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		resp.addCookie(cookie);
		return ResponseBo.ok();
	}
	/**
	 * 移除购物车
	 * 
	 * @param req
	 * @param resp
	 * @param account
	 * @param item
	 * @return
	 */
	@Override
	public ResponseBo removeCart(HttpServletRequest req, HttpServletResponse resp, MyUser myUser, CartItem item) {
		String key = getKey(req, resp, myUser);// 得到最终key
		ShoppingCart cacheCart = mergeCart(key, myUser);// 根据key取得最终购物车对象
		if (cacheCart != null && cacheCart.getCartItems() != null && cacheCart.getCartItems().size() > 0) {// ⑴
			//
			long count = cacheCart.getCartItems().stream()
					.filter(it -> it.getSpecificationIds().equals(item.getSpecificationIds())).count();
			if (count == 1) {// ⑵
				CartItem ci = cacheCart.getCartItems().stream()
						.filter(it -> it.getSpecificationIds().equals(item.getSpecificationIds())).findFirst()
						.orElse(null);
				if (ci.getQuantity() > item.getQuantity()) {// ⑶
					ci.setQuantity(ci.getQuantity() - item.getQuantity());
				} else if (ci.getQuantity() <= item.getQuantity()) {
					cacheCart.getCartItems().remove(ci);
				}
				// 1.满足缓存购物车中必须有商品才能减购物车
				// 2.满足缓存购物车中有该商品才能减购物车
				// 3.判断此次要减数量是否大于缓存购物车中数量 进行移除还是数量相减操作
			}
			Jedis jedis = jedisPool.getResource();
			Gson gson = new Gson();
			jedis.hset("CACHE_SHOPPINGCART", key, gson.toJson(cacheCart));
			jedis.close();
		}
		return ResponseBo.ok(cacheCart);
	}

}

package com.febs.web.controller.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.common.domain.ResponseBo;
import com.febs.shop.domain.CartItem;
import com.febs.shop.domain.ProductSku;
import com.febs.shop.domain.ShoppingCart;
import com.febs.shop.service.ProductSkuService;
import com.febs.shop.service.ShoppingCartService;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.WebBaseController;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/shopcart")
public class ShoppingCartController extends WebBaseController {
	@Autowired
	private ShoppingCartService service;
	@Autowired
	private ProductSkuService productSkuService;

	/**
	 * 进入首页
	 * 
	 * @return
	 */
	@RequestMapping("/cartList")
	@ResponseBody
	public ResponseBo cartList(HttpServletRequest req, HttpServletResponse resp) {
		MyUser myUser = this.getCurrentUser();
		String key = service.getKey(req, resp, myUser);
		ShoppingCart cacheCart = service.mergeCart(key, myUser);
		Map<String, Object> data = new HashMap<>();
		List<CartItem> items = cacheCart.getCartItems();
		data.put("items", items);
		BigDecimal total = new BigDecimal("0");
		for (CartItem item : items) {
			BigDecimal count = new BigDecimal(item.getQuantity());
			BigDecimal itemAllPrice = item.getPrice().multiply(count);
			total = total.add(itemAllPrice);
		}
		// 运费计算(大于200名运费,默认10元运费)
		BigDecimal deliveryFee = new BigDecimal("10");
		if (total.compareTo(new BigDecimal(200)) == 1) {
			deliveryFee = new BigDecimal("0");
		}
		data.put("productMoney", total.setScale(2, BigDecimal.ROUND_HALF_UP));
		data.put("total", total.setScale(2, BigDecimal.ROUND_HALF_UP).add(deliveryFee));
		data.put("deliveryFee", deliveryFee);

		return ResponseBo.ok(data);
	}

	@PostMapping("/addCart")
	@ResponseBody
	public ResponseBo add(HttpServletRequest req, HttpServletResponse resp) {
		if (null == req.getParameter("productId") || null == req.getParameter("productName")
				|| null == req.getParameter("specIds")) {
			return ResponseBo.error("商品参数信息有误");
		}
		String imgUrl = req.getParameter("imgUrl");
		Example example = new Example(ProductSku.class);
		example.createCriteria().andEqualTo("productId", req.getParameter("productId").toString())
				.andEqualTo("specificationIds", req.getParameter("specIds").toString());
		List<ProductSku> items = productSkuService.selectByExample(example);
		if (null == items || items.size() == 0) {
			return ResponseBo.error("商品信息有误，请重新选择");
		}
		ProductSku sku = items.get(0);
		if (sku.getStock() <= 0) {
			return ResponseBo.error("商品库存不足,请重新选择");
		}
		CartItem cartItem = new CartItem();
		Long productId = Long.parseLong(req.getParameter("productId").toString());
		cartItem.setProductId(productId);
		cartItem.setProductName(req.getParameter("productName").toString());
		cartItem.setPrice(sku.getPrice());

		cartItem.setImgUrl(StringUtils.isEmpty(sku.getImgUrl()) ? imgUrl : sku.getImgUrl());
		cartItem.setSpecification(sku.getSpecification());
		cartItem.setSpecificationIds(sku.getSpecificationIds());
		cartItem.setQuantity(1);
		MyUser myUser = this.getCurrentUser();
		ResponseBo result = service.addCart(req, resp, myUser, cartItem);
		return result;
	}

	@PostMapping("/removeCart")
	@ResponseBody
	public ResponseBo remove(HttpServletRequest req, HttpServletResponse resp, CartItem cartItem) {
		MyUser myUser = this.getCurrentUser();
		ResponseBo result = service.removeCart(req, resp, myUser, cartItem);
		return result;
	}
}

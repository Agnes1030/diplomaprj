package com.febs.web.controller.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.DateUtil;
import com.febs.shop.domain.CartItem;
import com.febs.shop.domain.Coupon;
import com.febs.shop.domain.CouponUser;
import com.febs.shop.domain.Order;
import com.febs.shop.domain.OrderDelivery;
import com.febs.shop.domain.OrderInvoice;
import com.febs.shop.domain.OrderItem;
import com.febs.shop.domain.ProductSku;
import com.febs.shop.domain.ShoppingCart;
import com.febs.shop.domain.UserAddress;
import com.febs.shop.enums.PayStatus;
import com.febs.shop.enums.TradeStatus;
import com.febs.shop.service.CouponService;
import com.febs.shop.service.CouponUserService;
import com.febs.shop.service.OrderDeliveryService;
import com.febs.shop.service.OrderInvoiceService;
import com.febs.shop.service.OrderItemService;
import com.febs.shop.service.OrderService;
import com.febs.shop.service.ProductService;
import com.febs.shop.service.ProductSkuService;
import com.febs.shop.service.ShoppingCartService;
import com.febs.shop.service.UserAddressService;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;
import com.febs.web.utils.OrderNumUtil;

import redis.clients.jedis.JedisPool;
import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/member")
public class ShopController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserAddressService userAddressService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderInvoiceService orderInvoiceService;
	@Autowired
	private OrderDeliveryService orderDeliveryService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private ProductSkuService productSkuService;
	@Autowired
	private CouponUserService couponUserService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private ProductService productService;
	@Autowired
	JedisPool jedisPool;

	/**
	 * 查看用户地址
	 */
	@RequestMapping("/userAddress")
	public String userAddress() {
		return "member/userAddress";
	}

	/**
	 * 分页查询用户订单列表
	 */
	@RequestMapping("/getUserOrderList")
	@ResponseBody
	public Map<String, Object> getUserOrderList(QueryRequest request) {
		MyUser myUser = this.getCurrentUser();
		Long userId = myUser.getUserId();
		return super.selectByPageNumSize(request, () -> this.orderService.getListByUser(userId));
	}

	@RequestMapping("/getUserAddressList")
	@ResponseBody
	public Map<String, Object> getUserAddressList(QueryRequest request) {
		MyUser myUser = this.getCurrentUser();
		Long userId = myUser.getUserId();
		return super.selectByPageNumSize(request, () -> userAddressService.findUserAddress(userId));
	}

	@RequestMapping("/userAddress/add")
	@ResponseBody
	public ResponseBo addUserAddress(UserAddress userAddress) {
		try {
			MyUser myUser = this.getCurrentUser();
			userAddress.setUserId(myUser.getUserId());
			userAddress.setCreateTime(new Date());
			this.userAddressService.save(userAddress);
			return ResponseBo.ok("新增地址成功！");
		} catch (Exception e) {
			log.error("新增地址失败", e);
			return ResponseBo.error("新增地址失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/deleteAddress")
	@ResponseBody
	public ResponseBo deleteUserAddress(String ids) {
		try {
			this.userAddressService.deleteUserAddress(ids);
			return ResponseBo.ok("删除地址成功！");
		} catch (Exception e) {
			log.error("删除地址失败", e);
			return ResponseBo.error("删除标签失败，请联系网站管理员！");
		}
	}

	/**
	 * 查看用户订单列表
	 */
	@RequestMapping("/orderList")
	public String orderList() {
		return "member/userOrder";
	}

	@RequestMapping("/toGenerateOrder")
	public String toGenerateOrder(HttpServletRequest request) {
		MyUser myUser = this.getCurrentUser();
		Example couponUserExample = new Example(CouponUser.class);
		couponUserExample.createCriteria().andEqualTo("couStatus", 1).andEqualTo("userId", myUser.getUserId());
		List<CouponUser> couponUsers = couponUserService.selectByExample(couponUserExample);
		request.setAttribute("coupons", couponUsers);
		return "member/toGenerateOrder";
	}

	/**
	 * 获取优惠券后的真实金额
	 */
	@RequestMapping("/getRealAmount")
	@ResponseBody
	public ResponseBo getRealAmount(HttpServletRequest req, HttpServletResponse resp, Long couponUserId) {
		MyUser myUser = this.getCurrentUser();
		String key = shoppingCartService.getKey(req, resp, myUser);
		ShoppingCart cacheCart = shoppingCartService.mergeCart(key, myUser);
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
		// 判断优惠券的所有人
		Example couponUserExample = new Example(CouponUser.class);
		couponUserExample.createCriteria().andEqualTo("id", couponUserId).andEqualTo("couStatus", 1)
				.andEqualTo("userId", myUser.getUserId());
		List<CouponUser> couponUsers = couponUserService.selectByExample(couponUserExample);
		if (null == couponUsers || couponUsers.size() == 0) {
			return ResponseBo.error("优惠券信息错误");
		}
		CouponUser couponUser = couponUsers.get(0);
		if (couponUser.getCouStatus() > 1) {
			return ResponseBo.error("优惠券失效");
		}
		Coupon coupon = couponService.selectByKey(couponUser.getCouponId());
		BigDecimal totalHalf = total.setScale(2, BigDecimal.ROUND_HALF_UP);
		boolean hasMin = false;
		Integer type = coupon == null ? null : coupon.getType();
		if (null != type) {
			hasMin = type == 1 ? true : false;
		}

		if ((hasMin && totalHalf.compareTo(coupon.getWithAmount()) < 0)) {
			return ResponseBo.error("优惠券金额未达到");
		}
		if (hasMin && totalHalf.compareTo(coupon.getWithAmount()) >= 0) {
			// 有门槛，判断是否符合门槛
			data.put("realAmount", totalHalf.subtract(coupon.getAmount()).add(deliveryFee));
		} else if (hasMin == false && null != type) {
			BigDecimal realAmount = totalHalf.compareTo(coupon.getAmount()) < 0 ? new BigDecimal(0)
					: totalHalf.subtract(coupon.getAmount());
			// 无门槛，直接减
			data.put("realAmount", realAmount.add(deliveryFee));
		} else {
			// 没券直接和总价相等
			data.put("realAmount", totalHalf.add(deliveryFee));
		}

		data.put("total", totalHalf.add(deliveryFee));
		data.put("productMoney", totalHalf);
		data.put("deliveryFee", deliveryFee);

		return ResponseBo.ok(data);
	}

	@PostMapping("/generateOrder")
	@ResponseBody
	public ResponseBo saveOrder(HttpServletRequest request, HttpServletResponse response, Order order) {
		MyUser myUser = this.getCurrentUser();
		String key = shoppingCartService.getKey(request, response, myUser);
		ShoppingCart cacheCart = shoppingCartService.mergeCart(key, myUser);
		List<CartItem> items = cacheCart.getCartItems();
		if (null == items || items.size() == 0) {
			return ResponseBo.error("购物车为空");
		}
		String couponUserId = request.getParameter("couponUserId");
		Coupon coupon = null;
		CouponUser couponUser = null;
		if (StringUtils.isNotBlank(couponUserId)) {
			// 判断优惠券的所有人
			Example couponUserExample = new Example(CouponUser.class);
			couponUserExample.createCriteria().andEqualTo("id", couponUserId).andEqualTo("couStatus", 1)
					.andEqualTo("userId", myUser.getUserId());
			List<CouponUser> couponUsers = couponUserService.selectByExample(couponUserExample);
			if (null == couponUsers || couponUsers.size() == 0) {
				return ResponseBo.error("优惠券信息错误");
			}
			couponUser = couponUsers.get(0);
			if (couponUser.getCouStatus() > 1) {
				return ResponseBo.error("优惠券失效");
			}
			coupon = couponService.selectByKey(couponUser.getCouponId());
		}
		// 生成订单
		String nsNumber = new OrderNumUtil(2, 3).nextId();
		order.setNs(nsNumber);
		order.setBuyerId(myUser.getUserId());
		order.setBuyerNickname(myUser.getUsername());
		order.setCreateTime(new Date());
		order.setModifiedTime(new Date());
		order.setPayStatus(1);
		order.setTradeStatus(1);
		order.setInvoiceStatus(1);
		orderService.save(order);
		BigDecimal totalFee = new BigDecimal(0);
		StringBuffer buff = new StringBuffer();

		// 遍历购物车
		for (CartItem item : items) {
			Example skuExample = new Example(ProductSku.class);
			skuExample.createCriteria().andEqualTo("productId", item.getProductId()).andEqualTo("specificationIds",
					item.getSpecificationIds());
			List<ProductSku> lists = productSkuService.selectByExample(skuExample);
			if (null == lists || lists.size() == 0) {
				return ResponseBo.error(item.getProductName() + "-购物车信息有误，请移除后重新下单");
			}

			ProductSku sku = lists.get(0);
			if (item.getQuantity() > sku.getStock()) {
				return ResponseBo.error(item.getProductName() + "-库存不足，请移除后重新下单");
			} else {
				// 减库存
				productSkuService.updateStock(sku.getId(), sku.getStock() - item.getQuantity());
				// 增加销量
				productService.updateAddSaleCount(item.getProductId(), item.getQuantity());
			}
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderId(order.getId());
			orderItem.setOrderNs(nsNumber);
			orderItem.setProductTitle(item.getProductName());
			orderItem.setProductId(item.getProductId());
			orderItem.setBuyerId(myUser.getUserId());
			orderItem.setBuyerNickname(myUser.getUsername());
			orderItem.setProductCount(item.getQuantity());
			orderItem.setProductPrice(sku.getPrice());
			orderItem.setProductSpec(sku.getSpecification());
			orderItem.setProductType("product");
			orderItem.setProductTypeText("产品");

			BigDecimal itemFee = orderItem.getProductPrice().multiply(new BigDecimal(orderItem.getProductCount()));
			orderItem.setTotalAmount(itemFee.setScale(2, BigDecimal.ROUND_HALF_UP));
			totalFee = totalFee.add(itemFee);
			buff.append(item.getProductName() + " ");
			orderItemService.save(orderItem);
		}
		order.setOrderTitle(buff.toString());
		BigDecimal totalHalf = totalFee.setScale(2, BigDecimal.ROUND_HALF_UP);
		// 运费计算(大于200名运费,默认10元运费)
		BigDecimal deliveryFee = new BigDecimal("10");
		if (totalHalf.compareTo(new BigDecimal(200)) == 1) {
			deliveryFee = new BigDecimal("0");
		}
		order.setOrderTotalAmount(totalHalf.add(deliveryFee));
		order.setCouponAmount(new BigDecimal(0));
		order.setDeliveryFee(deliveryFee);
		boolean hasMin = false;
		Integer type = coupon == null ? null : coupon.getType();
		if (null != type) {
			hasMin = type == 1 ? true : false;
		}

		if ((hasMin && totalHalf.compareTo(coupon.getWithAmount()) < 0)) {
			return ResponseBo.error("优惠券金额未达到");
		}
		if (hasMin && totalHalf.compareTo(coupon.getWithAmount()) >= 0) {
			couponUser.setCouStatus(2);
			couponUser.setOrderNs(nsNumber);
			couponUser.setUsedOrderId(order.getId());
			couponUser.setUseTime(new Date());
			couponUserService.updateNotNull(couponUser);
			order.setCouponUserId(couponUser.getId());
			order.setCouponAmount(coupon.getAmount());
			// 有门槛，判断是否符合门槛
			order.setOrderRealAmount(totalHalf.subtract(coupon.getAmount()).add(deliveryFee));
		} else if (hasMin == false && null != type) {
			couponUser.setCouStatus(2);
			couponUser.setOrderNs(nsNumber);
			couponUser.setUsedOrderId(order.getId());
			couponUser.setUseTime(new Date());
			couponUserService.updateNotNull(couponUser);
			order.setCouponUserId(couponUser.getId());
			order.setCouponAmount(coupon.getAmount());
			BigDecimal realAmount = totalHalf.compareTo(coupon.getAmount()) < 0 ? new BigDecimal(0)
					: totalHalf.subtract(coupon.getAmount());
			// 无门槛，直接减
			order.setOrderRealAmount(realAmount.add(deliveryFee));
		} else {
			// 没券直接和总价相等
			order.setOrderRealAmount(totalHalf.add(deliveryFee));
		}
		orderService.updateNotNull(order);
		// 操作成功清空购物车
		shoppingCartService.clearCart(request, response, myUser);
		return ResponseBo.ok(order);
	}

	@RequestMapping("/viewOrder")
	public String viewOrder(HttpServletRequest request, Long orderId) {
		try {
			Order order = this.orderService.selectByKey(orderId);
			List<OrderItem> items = this.orderItemService.getOrderItems(orderId);
			Long invoiceId = order.getInvoiceId();
			OrderInvoice invoice = this.orderInvoiceService.selectByKey(invoiceId);
			List<OrderDelivery> orderDeliverys = orderDeliveryService.getDeliveryByOrderId(orderId);
			request.setAttribute("order", order);
			request.setAttribute("invoice", invoice);
			request.setAttribute("items", items);
			request.setAttribute("orderDeliverys", orderDeliverys);
			if (null != order.getCouponUserId()) {
				CouponUser couponUser = couponUserService.selectByKey(order.getCouponUserId());
				request.setAttribute("coupon", couponUser.getTitle());
			}
			return "member/orderView";
		} catch (Exception e) {
			log.error("获取订单信息失败", e);
			return "error/500";
		}
	}

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 */
	@RequestMapping("/cancelUserOrder")
	@ResponseBody
	public ResponseBo cancelUserOrder(Long orderId, String userSummary) {
		MyUser myUser = this.getCurrentUser();
		Example orderExample = new Example(Order.class);
		orderExample.createCriteria().andEqualTo("buyerId", myUser.getUserId()).andEqualTo("id", orderId);
		List<Order> orders = orderService.selectByExample(orderExample);
		if (null == orders || orders.size() == 0) {
			return ResponseBo.error();
		}
		// 已经取消或者已经付款成功的不能取消，只能申请退款
		if (orders.get(0).getPayStatus() > 1) {
			return ResponseBo.error();
		}
		String orderSummary = orders.get(0).getOrderSummary();
		Order order = new Order();
		order.setId(orderId);
		order.setTradeStatus(TradeStatus.trade_cancel.getTradeStatus());
		StringBuffer sb = new StringBuffer();
		sb.append(orderSummary);
		sb.append(DateUtil.getDateFormat(new Date(), DateUtil.FULL_DATE_FORMAT) + "用户取消了订单");
		if (StringUtils.isNotEmpty(userSummary)) {
			sb.append("备注:" + userSummary);
		}
		sb.append("\n <br/>");
		order.setOrderSummary(sb.toString());
		orderService.updateNotNull(order);
		log.info(myUser.getUserId() + "用户取消了订单。" + orderId);
		return ResponseBo.ok();
	}

	/**
	 * 申请退款,如果用户已经货了，或者是虚拟商品，用户可以拒绝退款申请等用户寄回来再通过。如果符合要求，管理员在后台直接变为退款中
	 */
	@RequestMapping("/applyRefund")
	@ResponseBody
	public ResponseBo applyRefund(Long orderId, String userSummary) {
		MyUser myUser = this.getCurrentUser();
		Example orderExample = new Example(Order.class);
		orderExample.createCriteria().andEqualTo("buyerId", myUser.getUserId()).andEqualTo("id", orderId);
		List<Order> orders = orderService.selectByExample(orderExample);
		if (null == orders || orders.size() == 0) {
			return ResponseBo.error();
		}
		if (orders.get(0).getTradeStatus() == TradeStatus.trade_refund.getTradeStatus()) {
			return ResponseBo.error();
		}
		String orderSummary = orders.get(0).getOrderSummary();
		Order order = new Order();
		order.setId(orderId);
		order.setTradeStatus(TradeStatus.trade_refund.getTradeStatus());
		StringBuffer sb = new StringBuffer();
		sb.append(orderSummary);
		sb.append(DateUtil.getDateFormat(new Date(), DateUtil.FULL_DATE_FORMAT) + "用户取消了订单。");
		if (StringUtils.isNotEmpty(userSummary)) {
			sb.append("备注:" + userSummary);
		}
		sb.append("\n <br/>");
		order.setOrderSummary(sb.toString());
		orderService.updateNotNull(order);
		log.info(myUser.getUserId() + "用户申请了退款,订单" + orderId);
		return ResponseBo.ok();
	}

	/**
	 * 模拟支付，直接更新订单状态以测试流程通不通，实际支付可会调用微信或支付宝界面支付，成功后进行回调再更新订单状态
	 */
	@RequestMapping("/orderWxPay")
	@ResponseBody
	public ResponseBo orderWxPay(Long orderId) {
		MyUser myUser = this.getCurrentUser();
		if (null == orderId) {
			return ResponseBo.error();
		}
		Order order = orderService.selectByKey(orderId);
		if (order == null || order.getBuyerId().longValue() != myUser.getUserId().longValue()) {
			return ResponseBo.error();
		}
		// 信息核实正确，直接更新支付状态
		order.setPayStatus(PayStatus.wx_pay.getPayStatus());
		// 支付完成则交易成功(非结束)
		order.setTradeStatus(TradeStatus.trade_success.getTradeStatus());
		order.setModifiedTime(new Date());
		orderService.updateNotNull(order);
		return ResponseBo.ok();
	}

	/**
	 * 模拟支付宝
	 */
	@RequestMapping("/orderZfbPay")
	@ResponseBody
	public ResponseBo orderZfbPay(Long orderId) {
		MyUser myUser = this.getCurrentUser();
		if (null == orderId) {
			return ResponseBo.error();
		}
		Order order = orderService.selectByKey(orderId);
		if (order == null || order.getBuyerId().longValue() != myUser.getUserId().longValue()) {
			return ResponseBo.error();
		}
		// 信息核实正确，直接更新支付状态
		order.setPayStatus(PayStatus.zfb_pay.getPayStatus());
		order.setTradeStatus(TradeStatus.trade_success.getTradeStatus());
		order.setModifiedTime(new Date());
		orderService.updateNotNull(order);
		return ResponseBo.ok();
	}

	/**
	 * 确认收货后直接更新交易状态为交易结束(如果用户一直未操作，后台管理员确认签收后也可以直接改状态)
	 */
	@RequestMapping("/sureGetProducts")
	@ResponseBody
	public ResponseBo sureGetProducts(Long orderId) {
		MyUser myUser = this.getCurrentUser();
		if (null == orderId) {
			return ResponseBo.error();
		}
		Order order = orderService.selectByKey(orderId);
		if (order == null || order.getBuyerId().longValue() != myUser.getUserId().longValue()) {
			return ResponseBo.error();
		}
		// 确认收货交易结束
		order.setTradeStatus(TradeStatus.trade_finish.getTradeStatus());
		order.setModifiedTime(new Date());
		String orderSummary = order.getOrderSummary();
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(orderSummary)) {
			sb.append(orderSummary);
		}
		String enumOrderStatusText = TradeStatus.getTradeEnumText(order.getTradeStatus());
		sb.append(
				DateUtil.getDateFormat(new Date(), DateUtil.FULL_DATE_FORMAT) + "用户确认收货，状态变更为" + enumOrderStatusText);
		sb.append("\n <br/>");
		order.setOrderSummary(sb.toString());
		orderService.updateNotNull(order);
		return ResponseBo.ok();
	}

}

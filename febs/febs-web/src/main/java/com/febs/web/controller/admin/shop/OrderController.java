package com.febs.web.controller.admin.shop;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.DateUtil;
import com.febs.common.utils.FileUtils;
import com.febs.shop.domain.CouponUser;
import com.febs.shop.domain.Order;
import com.febs.shop.domain.OrderDelivery;
import com.febs.shop.domain.OrderInvoice;
import com.febs.shop.domain.OrderItem;
import com.febs.shop.enums.TradeStatus;
import com.febs.shop.service.CouponUserService;
import com.febs.shop.service.OrderDeliveryService;
import com.febs.shop.service.OrderInvoiceService;
import com.febs.shop.service.OrderItemService;
import com.febs.shop.service.OrderService;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;
import com.febs.web.utils.UploadUtil;
/**
 * 后台订单管理controller
 * @author wtsoftware 
 * @date 2020-06-14
 */
@Controller
@RequestMapping("/admin")
public class OrderController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderInvoiceService orderInvoiceService;
	@Autowired
	private OrderDeliveryService orderDeliveryService;
	@Autowired
	private CouponUserService couponUserService;

	@Log("订单列表")
	@RequestMapping("/order")
	@PreAuthorize("hasAuthority('order:list')")
	public String index() {
		return "admin/shop/order/order";
	}

	@Log("获取订单列表信息")
	@RequestMapping("/order/list")
	@PreAuthorize("hasAuthority('order:list')")
	@ResponseBody
	public Map<String, Object> orderList(HttpServletRequest request, QueryRequest queryRequest, Order order) {
		return super.selectByPageNumSize(queryRequest, () -> this.orderService.selectAll());
	}

	@RequestMapping("/order/viewOrder")
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
			return "admin/shop/order/orderView";
		} catch (Exception e) {
			log.error("获取订单信息失败", e);
			return "error/500";
		}
	}

	@PostMapping("/order/filesUpload/thumbnail")
	@ResponseBody
	public ResponseBo logoUpload(@RequestParam("orderFile") MultipartFile[] file) {
		String fileName = UploadUtil.uploadImg(file);
		// 返回前端data数据中存储修改后的文件名
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("src", "/upload/" + fileName);
		return ResponseBo.ok(msg);
	}

	@Log("新增订单 ")
	@PreAuthorize("hasAuthority('order:add')")
	@RequestMapping("/order/add")
	@ResponseBody
	public ResponseBo addOrder(Order order, OrderItem[] orderItems, HttpServletRequest request) {
		try {
			MyUser currentUser = this.getCurrentUser();
			order.setBuyerId(currentUser.getUserId());
			this.orderService.save(order);
			Long orderId = order.getId();
			// 订单商品处理
			if (null != orderItems && orderItems.length > 0) {
				for (int i = 0; i < orderItems.length; i++) {
					OrderItem item = orderItems[i];
					item.setOrderId(orderId);
					this.orderItemService.save(item);
				}
			}

			return ResponseBo.ok("新增订单成功！");
		} catch (Exception e) {
			log.error("新增订单失败", e);
			return ResponseBo.error("新增订单失败，请联系网站管理员！");
		}
	}

	@Log("修改订单 ")
	@PreAuthorize("hasAuthority('order:update')")
	@RequestMapping("/order/update")
	@ResponseBody
	public ResponseBo updateOrder(Order order, OrderItem[] orderItems, HttpServletRequest request) {
		try {
			this.orderService.updateNotNull(order);
			Long orderId = order.getId();
			// 图片集处理
			// 删除原关系重新添加
			orderItemService.deleteOrderItems(orderId);
			// 订单商品处理
			if (null != orderItems && orderItems.length > 0) {
				for (int i = 0; i < orderItems.length; i++) {
					OrderItem item = orderItems[i];
					item.setOrderId(orderId);
					this.orderItemService.save(item);
				}
			}

			return ResponseBo.ok("修改订单成功！");
		} catch (Exception e) {
			log.error("修改订单失败", e);
			return ResponseBo.error("修改订单失败，请联系网站管理员！");
		}
	}

	@Log("修改订单状态")
	@PreAuthorize("hasAuthority('order:update')")
	@RequestMapping("/order/updateOrderTradeStatus")
	@ResponseBody
	public ResponseBo updateOrderTradeStatus(Order order, HttpServletRequest request) {
		try {
			Long orderId = order.getId();
			Order currentOrder = orderService.selectByKey(orderId);
			order.setModifiedTime(new Date());
			String orderSummary = currentOrder.getOrderSummary();
			StringBuffer sb = new StringBuffer();
			if (StringUtils.isNotBlank(orderSummary)) {
				sb.append(orderSummary);
			}
			if (order.getTradeStatus().intValue() == currentOrder.getTradeStatus().intValue()) {
				return ResponseBo.error("更新状态等于当前订单状态");
			}
			String enumOrderStatusText = TradeStatus.getTradeEnumText(order.getTradeStatus());
			sb.append(
					DateUtil.getDateFormat(new Date(), DateUtil.FULL_DATE_FORMAT) + "管理员更新订单状态为" + enumOrderStatusText);
			if (StringUtils.isNotEmpty(order.getOrderSummary())) {
				sb.append("备注:" + order.getOrderSummary());
			}
			sb.append("\n <br/>");
			order.setOrderSummary(sb.toString());
			this.orderService.updateNotNull(order);
			return ResponseBo.ok("修改订单成功！");
		} catch (Exception e) {
			log.error("修改订单失败", e);
			return ResponseBo.error("修改订单失败，请联系网站管理员！");
		}
	}

	@Log("修改订单 ")
	@PreAuthorize("hasAuthority('order:update')")
	@RequestMapping("/order/updateRemark")
	@ResponseBody
	public ResponseBo updateOrderRemarks(String orderRemarks, String orderId, HttpServletRequest request) {
		try {
			this.orderService.updateRemarks(orderRemarks, orderId);
			return ResponseBo.ok("修改订单成功！");
		} catch (Exception e) {
			log.error("修改订单失败", e);
			return ResponseBo.error("修改订单失败，请联系网站管理员！");
		}
	}

	@Log("修改订单快递信息 ")
	@PreAuthorize("hasAuthority('order:update')")
	@RequestMapping("/order/updateDeliver")
	@ResponseBody
	public ResponseBo updateOrderDeliver(OrderDelivery orderDelivery, HttpServletRequest request) {
		try {
			MyUser myUser = this.getCurrentUser();
			orderDelivery.setUserId(myUser.getUserId());
			orderDelivery.setUserName(myUser.getUsername());
			orderDelivery.setCreateTime(new Date());
			orderDelivery.setStartTime(new Date());

			this.orderDeliveryService.save(orderDelivery);
			return ResponseBo.ok();
		} catch (Exception e) {
			log.error("修改订单失败", e);
			return ResponseBo.error("修改订单失败，请联系网站管理员！");
		}
	}

	@Log("删除订单快递信息 ")
	@PreAuthorize("hasAuthority('order:update')")
	@RequestMapping("/order/deleteDeliver")
	@ResponseBody
	public ResponseBo deleteOrderDeliver(Long deliverId, HttpServletRequest request) {
		try {
			this.orderDeliveryService.delete(deliverId);
			return ResponseBo.ok("修改订单快递信息成功！");
		} catch (Exception e) {
			log.error("修改订单失败", e);
			return ResponseBo.error("修改订单失败，请联系网站管理员！");
		}
	}

	@Log("删除订单")
	@PreAuthorize("hasAuthority('order:delete')")
	@RequestMapping("/order/delete")
	@ResponseBody
	public ResponseBo deleteOrders(String ids) {
		try {
			this.orderService.deleteOrders(ids);
			return ResponseBo.ok("删除订单成功！");
		} catch (Exception e) {
			log.error("删除订单失败", e);
			return ResponseBo.error("删除订单失败，请联系网站管理员！");
		}
	}

	/**
	 * 查看快递信息
	 */
	@RequestMapping("/order/editDelivery")
	@ResponseBody
	public ResponseBo editOrderDelivery(Long deliveryId) {
		OrderDelivery orderDelivery = this.orderDeliveryService.selectByKey(deliveryId);
		return ResponseBo.ok(orderDelivery);
	}

	@RequestMapping("/order/updateDeliveryById")
	@ResponseBody
	public ResponseBo updateDeliveryById(OrderDelivery orderDelivery, HttpServletRequest request) {
		try {
			MyUser myUser = this.getCurrentUser();
			orderDelivery.setUserId(myUser.getUserId());
			orderDelivery.setUserName(myUser.getUsername());

			this.orderDeliveryService.updateNotNull(orderDelivery);
			Order currentOrder = orderService.selectByKey(orderDelivery.getOrderId());
			String orderSummary = currentOrder.getOrderSummary();
			StringBuffer sb = new StringBuffer();
			if (StringUtils.isNotBlank(orderSummary)) {
				sb.append(orderSummary);
			}
			String enumOrderStatusText = TradeStatus.getTradeEnumText(currentOrder.getTradeStatus());
			sb.append(DateUtil.getDateFormat(new Date(), DateUtil.FULL_DATE_FORMAT) + "管理员(" + myUser.getUsername()
					+ ")更新快递状态为" + enumOrderStatusText);
			sb.append("\n <br/>");
			currentOrder.setOrderSummary(sb.toString());
			orderService.updateNotNull(currentOrder);
			return ResponseBo.ok("修改订单快递信息成功！");
		} catch (Exception e) {
			log.error("修改订单失败", e);
			return ResponseBo.error("修改订单失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/order/excel")
	@ResponseBody
	public ResponseBo orderExcel(Order order, HttpServletRequest request) {
		try {
			List<Order> list = this.orderService.selectAll();
			return FileUtils.createExcelByPOIKit("订单表", list, Order.class);
		} catch (Exception e) {
			log.error("导出订单信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/order/csv")
	@ResponseBody
	public ResponseBo orderCsv(Order order, HttpServletRequest request) {
		try {
			List<Order> list = this.orderService.selectAll();
			return FileUtils.createCsv("订单表", list, Order.class);
		} catch (Exception e) {
			log.error("获取订单信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

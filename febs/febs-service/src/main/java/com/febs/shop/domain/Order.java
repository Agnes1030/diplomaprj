package com.febs.shop.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_user_order")
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6907319671430782536L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "ns")
	@ExportConfig(value = "订单号")
	private String ns;
	@Column(name = "product_type")
	private String productType;
	@Column(name = "order_title")
	@ExportConfig(value = "订单标题")
	private String orderTitle;
	@Column(name = "order_summary")
	@ExportConfig(value = "订单摘要")
	private String orderSummary;
	// 购买人
	@Column(name = "buyer_id")
	private Long buyerId;
	@Column(name = "buyer_nickname")
	@ExportConfig(value = "购买人昵称")
	private String buyerNickname;
	@Column(name = "buyer_msg")
	@ExportConfig(value = "用户留言")
	private String buyerMsg;
	@Column(name = "order_total_amount")
	@ExportConfig(value = "订单总金额，购买人员应该付款的金额")
	private BigDecimal orderTotalAmount;
	@Column(name = "order_real_amount")
	@ExportConfig(value = "订单的真实金额，销售人员可以在后台修改支付金额，一般情况下 order_real_amount = order_total_amount")
	private BigDecimal orderRealAmount;
	@Column(name = "coupon_user_id")
	@ExportConfig(value = "优惠券ID")
	private Long couponUserId;
	@Column(name = "coupon_amount")
	@ExportConfig(value = "优惠金额")
	private BigDecimal couponAmount;
	@Column(name="delivery_fee")
	private BigDecimal deliveryFee;
	@Column(name = "pay_status")
	@ExportConfig(value = "支付状态：1未付款、 2支付宝、3微信支付 、4网银（线上支付)5、余额支付")
	private Integer payStatus;
	@Column(name = "pay_success_amount")
	@ExportConfig(value = "支付成功的金额")
	private BigDecimal paySuccessAmount;
	@Column(name = "pay_success_time")
	@ExportConfig(value = "支付时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date paySuccessTime;
	@Column(name = "pay_success_proof")
	@ExportConfig(value = "支付证明，手动入账时需要截图")
	private String paySuccessProof;
	@Column(name = "pay_success_remarks")
	@ExportConfig(value = "支付备注")
	private String paySuccessRemarks;
	// 支付记录
	@Column(name = "payment_id")
	private Integer paymentId;
	@Column(name = "payment_outer_id")
	@ExportConfig(value = "第三方订单号")
	private String paymentOuterId;
	@Column(name = "payment_outer_user")
	@ExportConfig(value = "第三方支付用户，一般情况下是用户的openId")
	private String paymentOuterUser;
	@Column(name = "delivery_addr_username")
	@ExportConfig(value = "收货人名称")
	private String deliveryAddrUsername;
	@Column(name = "delivery_addr_mobile")
	@ExportConfig(value = "收货人手机号（电话）")
	private String deliveryAddrMobile;
	@Column(name = "delivery_addr_province")
	@ExportConfig(value = "收件人省）")
	private String deliveryAddrProvince;
	@Column(name = "delivery_addr_city")
	@ExportConfig(value = "收件人的城市）")
	private String deliveryAddrCity;
	@Column(name = "delivery_addr_district")
	@ExportConfig(value = "收件人的县）")
	private String deliveryAddrDistrict;
	@Column(name = "delivery_addr_detail")
	@ExportConfig(value = "收件人的详细地址）")
	private String deliveryAddrDetail;
	@Column(name = "delivery_addr_zipcode")
	@ExportConfig(value = "收件人地址邮政编码）")
	private String deliveryAddrZipcode;
	// 发票ID
	@Column(name = "invoice_id")
	private Long invoiceId;
	// 发票状态
	@Column(name = "invoice_status")
	private Integer invoiceStatus;
	// 管理员后台备注
	@Column(name = "remarks")
	private String remarks;

	// json字段扩展
	@Column(name = "options")
	private String options;
	// 交易状态：1交易中、 2交易完成（但是可以申请退款） 、3取消交易 、4申请退款、 5拒绝退款、 6退款中、 7退款完成、 9交易结束
	@Column(name = "trade_status")
	private Integer tradeStatus;
	// 删除状态：1 正常 ，2 回收站 3 已经删除
	@Column(name = "del_status")
	private Integer delStatus;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	@Column(name = "modified_time")
	@ExportConfig(value = "最后更新时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date modifiedTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}



	public String getOrderSummary() {
		return orderSummary;
	}

	public void setOrderSummary(String orderSummary) {
		this.orderSummary = orderSummary;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerNickname() {
		return buyerNickname;
	}

	public void setBuyerNickname(String buyerNickname) {
		this.buyerNickname = buyerNickname;
	}

	public String getBuyerMsg() {
		return buyerMsg;
	}

	public void setBuyerMsg(String buyerMsg) {
		this.buyerMsg = buyerMsg;
	}

	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public BigDecimal getOrderRealAmount() {
		return orderRealAmount;
	}

	public void setOrderRealAmount(BigDecimal orderRealAmount) {
		this.orderRealAmount = orderRealAmount;
	}


	public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}

	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public BigDecimal getPaySuccessAmount() {
		return paySuccessAmount;
	}

	public void setPaySuccessAmount(BigDecimal paySuccessAmount) {
		this.paySuccessAmount = paySuccessAmount;
	}

	public Date getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(Date paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
	}

	public String getPaySuccessProof() {
		return paySuccessProof;
	}

	public void setPaySuccessProof(String paySuccessProof) {
		this.paySuccessProof = paySuccessProof;
	}

	public String getPaySuccessRemarks() {
		return paySuccessRemarks;
	}

	public void setPaySuccessRemarks(String paySuccessRemarks) {
		this.paySuccessRemarks = paySuccessRemarks;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentOuterId() {
		return paymentOuterId;
	}

	public void setPaymentOuterId(String paymentOuterId) {
		this.paymentOuterId = paymentOuterId;
	}

	public String getPaymentOuterUser() {
		return paymentOuterUser;
	}

	public void setPaymentOuterUser(String paymentOuterUser) {
		this.paymentOuterUser = paymentOuterUser;
	}
	public String getDeliveryAddrUsername() {
		return deliveryAddrUsername;
	}

	public void setDeliveryAddrUsername(String deliveryAddrUsername) {
		this.deliveryAddrUsername = deliveryAddrUsername;
	}

	public String getDeliveryAddrMobile() {
		return deliveryAddrMobile;
	}

	public void setDeliveryAddrMobile(String deliveryAddrMobile) {
		this.deliveryAddrMobile = deliveryAddrMobile;
	}

	public String getDeliveryAddrProvince() {
		return deliveryAddrProvince;
	}

	public void setDeliveryAddrProvince(String deliveryAddrProvince) {
		this.deliveryAddrProvince = deliveryAddrProvince;
	}

	public String getDeliveryAddrCity() {
		return deliveryAddrCity;
	}

	public void setDeliveryAddrCity(String deliveryAddrCity) {
		this.deliveryAddrCity = deliveryAddrCity;
	}

	public String getDeliveryAddrDistrict() {
		return deliveryAddrDistrict;
	}

	public void setDeliveryAddrDistrict(String deliveryAddrDistrict) {
		this.deliveryAddrDistrict = deliveryAddrDistrict;
	}

	public String getDeliveryAddrDetail() {
		return deliveryAddrDetail;
	}

	public void setDeliveryAddrDetail(String deliveryAddrDetail) {
		this.deliveryAddrDetail = deliveryAddrDetail;
	}

	public String getDeliveryAddrZipcode() {
		return deliveryAddrZipcode;
	}

	public void setDeliveryAddrZipcode(String deliveryAddrZipcode) {
		this.deliveryAddrZipcode = deliveryAddrZipcode;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public Integer getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Integer tradeStatus) {
		this.tradeStatus = tradeStatus;
	}



	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

}

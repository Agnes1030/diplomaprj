package com.febs.shop.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_user_order_item")
public class OrderItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1173060965919121451L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "order_id")
	@ExportConfig(value = "订单id")
	private Long orderId;
	// 订单编号
	@Column(name = "order_ns")
	private String orderNs;
	// 买家id
	@Column(name = "buyer_id")
	private Long buyerId;
	@Column(name = "buyer_nickname")
	private String buyerNickname;
	// 用户留言
	@Column(name = "buyer_msg")
	private String buyerMsg;
	// 卖家id
	@Column(name = "seller_id")
	private Long sellerId;
	// 分销员ID
	@Column(name = "dist_user_id")
	private Long distUserId;
	// 分销金额
	@Column(name = "dist_amount")
	private BigDecimal distAmount;
	// 产品id
	@Column(name = "product_id")
	private Long productId;
	// 商品的类别，默认是 product ，但是未来可能是 模板、文件、视频等等...
	@Column(name = "product_type")
	private String productType;
	// 商品的类别，默认是 product ，但是未来可能是 模板、文件、视频等等...
	@Column(name = "product_type_text")
	private String productTypeText;
	// 商品标题.
	@Column(name = "product_title")
	private String productTitle;
	// 商品备注
	@Column(name = "product_summary")
	private String productSummary;
	// 商品规格ID
	@Column(name = "product_sku_id")
	private Long skuId;
	// 商品规格
	@Column(name = "product_spec")
	private String productSpec;
	// 缩略图
	@Column(name = "product_thumbnail")
	private String productThumbnail;
	// 商品链接
	@Column(name = "product_link")
	private String productLink;
	// 商品单价
	@Column(name = "product_price")
	private BigDecimal productPrice;
	// 产品数量
	@Column(name = "product_count")
	private Integer productCount;
	// 是否是虚拟产品，1是，0不是。虚拟产品支付完毕后立即交易完成。是虚拟产品，虚拟产品支付完毕后立即交易完成
	@Column(name = "with_virtual")
	private Integer withVirtual;
	// 是否支持退款，1支持，0不支持。
	@Column(name = "with_refund")
	private Integer withRefund;
	// 快递费
	@Column(name = "delivery_cost")
	private BigDecimal deliveryCost;
	// 快递单 id
	@Column(name = "delivery_id")
	private Long deliveryId;
	// 其它费用
	@Column(name = "other_cost")
	private BigDecimal otherCost;
	// 其它费用备注
	@Column(name = "other_cost_remark")
	private String otherCostRemark;
	// 具体金额 = 产品价格+运费+其他费用- 分销金额
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	// 支付金额 = 产品价格+运费+其他价格
	@Column(name = "pay_amount")
	private BigDecimal payAmount;
	// 查看的网址路径，访问时时，会添加orderid
	@Column(name = "view_path")
	private String viewPath;
	// 查看的文章内容，比如：查看、下载
	@Column(name = "view_text")
	private String viewText;
	// 可访问的有效时间，单位秒
	@Column(name = "view_effective_time")
	private Integer viewEffectiveTime;
	// 评论的路径
	@Column(name = "comment_path")
	private String comment_path;
	// 发票id
	@Column(name = "invoice_id")
	private Long invoiceId;
	// 发票状态
	@Column(name = "invoice_status")
	private Integer invoiceStatus;
	// 状态：1交易中、 2交易完成（但是可以申请退款） 、3取消交易 、4申请退款、 5拒绝退款、 6退款中、 7退款完成、 9交易结束
	@Column(name = "item_status")
	private Integer itemStatus;
	// 退款订单号
	@Column(name = "refund_no")
	private String refundNo;
	// 退款金额
	@Column(name = "refund_amount")
	private BigDecimal refundAmount;
	// 退款描述
	@Column(name = "refund_desc")
	private String refundDesc;
	// 退款时间
	@Column(name = "refund_time")
	private Date refundTime;
	// options
	@Column(name = "options")
	private String options;
	// 修改时间
	@Column(name = "modified_time")
	private Date modifiedTime;
	// 创建时间
	@Column(name = "create_time")
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getDistUserId() {
		return distUserId;
	}
	public void setDistUserId(Long distUserId) {
		this.distUserId = distUserId;
	}
	public BigDecimal getDistAmount() {
		return distAmount;
	}
	public void setDistAmount(BigDecimal distAmount) {
		this.distAmount = distAmount;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductTypeText() {
		return productTypeText;
	}
	public void setProductTypeText(String productTypeText) {
		this.productTypeText = productTypeText;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public String getProductSummary() {
		return productSummary;
	}
	public void setProductSummary(String productSummary) {
		this.productSummary = productSummary;
	}
	public String getProductSpec() {
		return productSpec;
	}
	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
	}
	public String getProductThumbnail() {
		return productThumbnail;
	}
	public void setProductThumbnail(String productThumbnail) {
		this.productThumbnail = productThumbnail;
	}
	public String getProductLink() {
		return productLink;
	}
	public void setProductLink(String productLink) {
		this.productLink = productLink;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public Integer getProductCount() {
		return productCount;
	}
	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}
	public Integer getWithVirtual() {
		return withVirtual;
	}
	public void setWithVirtual(Integer withVirtual) {
		this.withVirtual = withVirtual;
	}
	public Integer getWithRefund() {
		return withRefund;
	}
	public void setWithRefund(Integer withRefund) {
		this.withRefund = withRefund;
	}
	public BigDecimal getDeliveryCost() {
		return deliveryCost;
	}
	public void setDeliveryCost(BigDecimal deliveryCost) {
		this.deliveryCost = deliveryCost;
	}
	public Long getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}
	public BigDecimal getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(BigDecimal otherCost) {
		this.otherCost = otherCost;
	}
	public String getOtherCostRemark() {
		return otherCostRemark;
	}
	public void setOtherCostRemark(String otherCostRemark) {
		this.otherCostRemark = otherCostRemark;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getViewPath() {
		return viewPath;
	}
	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
	}
	public String getViewText() {
		return viewText;
	}
	public void setViewText(String viewText) {
		this.viewText = viewText;
	}
	public Integer getViewEffectiveTime() {
		return viewEffectiveTime;
	}
	public void setViewEffectiveTime(Integer viewEffectiveTime) {
		this.viewEffectiveTime = viewEffectiveTime;
	}
	public String getComment_path() {
		return comment_path;
	}
	public void setComment_path(String comment_path) {
		this.comment_path = comment_path;
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
	public Integer getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundDesc() {
		return refundDesc;
	}
	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	public Date getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getOrderNs() {
		return orderNs;
	}
	public void setOrderNs(String orderNs) {
		this.orderNs = orderNs;
	}
	
}

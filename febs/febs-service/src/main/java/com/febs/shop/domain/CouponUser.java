package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;
/**
 * 优惠券领取记录
 */
@Table(name = "t_coupon_user")
public class CouponUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919866044316233804L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	// 优惠券类型ID
	@Column(name="coupon_id")
	private Long couponId;
	// 优惠券标题
	@Column(name="title")
	private String title;
	// 领取用户ID
	@Column(name="user_id")
	private Long userId;
	@Column(name="user_name")
	private String userName;
	// 所使用的订单ID
	@Column(name="used_order_id")
	private Long usedOrderId;
	@Column(name="order_ns")
	private String orderNs;
	// 支付记录ID
	@Column(name="user_payment_id")
	private Long userPaymentId;
	// 状态 1 未使用  2 已经使用  3 已过期  9 不可用
	@Column(name="cou_status")
	private Integer couStatus;
	// 获得时间 
	@Column(name="get_time")
	private Date getTime;
	// 使用时间 
	@Column(name="use_time")
	private Date useTime;
	// 如果是由后台管理员发放，则记录下由哪位管理员发放的。前台用户自己领取的，此字段将为空。
	@Column(name="send_uid")
	private Long sendUid;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getCouStatus() {
		return couStatus;
	}
	public void setCouStatus(Integer couStatus) {
		this.couStatus = couStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUsedOrderId() {
		return usedOrderId;
	}
	public void setUsedOrderId(Long usedOrderId) {
		this.usedOrderId = usedOrderId;
	}
	public Long getUserPaymentId() {
		return userPaymentId;
	}
	public void setUserPaymentId(Long userPaymentId) {
		this.userPaymentId = userPaymentId;
	}
	public Date getUseTime() {
		return useTime;
	}
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
	public Long getSendUid() {
		return sendUid;
	}
	public void setSendUid(Long sendUid) {
		this.sendUid = sendUid;
	}
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
	public String getOrderNs() {
		return orderNs;
	}
	public void setOrderNs(String orderNs) {
		this.orderNs = orderNs;
	}
	
	
}

package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

// 发货信息表
@Table(name = "t_user_order_delivery")
public class OrderDelivery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3676697552711117236L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	// 操作用户ID
	@Column(name = "user_id")
	private Long userId;
	// 订单号
	@Column(name = "order_id")
	private Long orderId;
	// 快递公司ID
	@Column(name = "delivery_id")
	private Long deliveryId;	
	@Column(name = "username")
	@ExportConfig(value = "操作用户名")
	private String userName;
	@Column(name = "company")
	@ExportConfig(value = "快递公司")
	private String company;
	@Column(name = "delivery_number")
	@ExportConfig(value = "快递单号")
	private String deliveryNumber;
	@Column(name = "start_time")
	@ExportConfig(value = "快递发货时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date startTime;
	@Column(name = "finish_time")
	@ExportConfig(value = "快递送达时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date finishTime;
	@Column(name = "addr_username")
	@ExportConfig(value = "收货人地址")
	private String addrUsername;
	@Column(name = "addr_mobile")
	@ExportConfig(value = "收货人手机号（电话）")
	private String addrMobile;
	@Column(name = "addr_province")
	@ExportConfig(value = "收件人省")
	private String addrProvince;
	@Column(name = "addr_city")
	@ExportConfig(value = "收件人的城市")
	private String addrCity;
	@Column(name = "addr_district")
	@ExportConfig(value = "收件人的区（县）")
	private String addrDistrict;
	@Column(name = "addr_detail")
	@ExportConfig(value = "收件人的详细地址")
	private String addrDetail;
	// 收件人地址邮政编码
	@Column(name = "addr_zipcode")
	private String addrZipcode;
	// 运输状态
	@Column(name = "delivery_status")
	private Integer deliveryStatus;
	// json字段扩展
	@Column(name = "options")
	private String options;
	// remarks
	@Column(name = "remarks")
	private String remarks;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDeliveryNumber() {
		return deliveryNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getAddrProvince() {
		return addrProvince;
	}

	public void setAddrProvince(String addrProvince) {
		this.addrProvince = addrProvince;
	}

	public String getAddrDistrict() {
		return addrDistrict;
	}

	public void setAddrDistrict(String addrDistrict) {
		this.addrDistrict = addrDistrict;
	}

	public String getAddrDetail() {
		return addrDetail;
	}

	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}

	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getAddrUsername() {
		return addrUsername;
	}

	public void setAddrUsername(String addrUsername) {
		this.addrUsername = addrUsername;
	}

	public String getAddrMobile() {
		return addrMobile;
	}

	public void setAddrMobile(String addrMobile) {
		this.addrMobile = addrMobile;
	}

	

	public String getAddrCity() {
		return addrCity;
	}

	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}

	public String getAddrZipcode() {
		return addrZipcode;
	}

	public void setAddrZipcode(String addrZipcode) {
		this.addrZipcode = addrZipcode;
	}

	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
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

	public Long getOrderId() {
		return orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}

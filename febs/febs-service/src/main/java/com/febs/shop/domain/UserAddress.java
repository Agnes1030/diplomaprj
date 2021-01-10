package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 用户常用地址表 
 */
@Table(name = "t_user_address")
public class UserAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8203975158382405435L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "contact_name")
	private String contactName;
	@Column(name = "contact_mobile")
	private String contactMobile;
	@Column(name = "zipcode")
	private String zipCode;
	@Column(name = "province")
	private String province;
	@Column(name = "city")
	private String city;
	// 区县
	@Column(name = "districts")
	private String districts;
	// 详细地址
	@Column(name = "addr_details")
	private String addrDetails;
	@Column(name = "create_time")
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistricts() {
		return districts;
	}
	public void setDistricts(String districts) {
		this.districts = districts;
	}
	public String getAddrDetails() {
		return addrDetails;
	}
	public void setAddrDetails(String addrDetails) {
		this.addrDetails = addrDetails;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}

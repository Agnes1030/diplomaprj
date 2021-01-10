package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 商品规格表 
 */
@Table(name = "t_product_specification")
public class ProductSpecification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3439698347161144492L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "product_id")
	private Long productId;
	@Column(name = "specification")
	private String specification;
	// 暂时做为前台DTO冗余字段
	@Column(name = "value")
	private String value;
	@Column(name = "pic_url")
	private String picUrl;
	@Column(name = "product_status")
	private Integer productStatus;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "modified_time")
	private Date modifiedTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public Integer getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
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
	
}

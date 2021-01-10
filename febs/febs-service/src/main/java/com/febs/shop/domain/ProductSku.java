package com.febs.shop.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "t_product_sku")
public class ProductSku implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -48064444023338330L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "product_id")
	private Long productId;
	@Column(name = "specification")
	private String specification;
	@Column(name = "specification_ids")
	private String specificationIds;
	
	@Column(name = "price")
	private BigDecimal price;
	@Column(name = "origin_price")
	private BigDecimal originPrice;
	@Column(name = "stock")
	private Integer stock;
	@Column(name = "img_url")
	private String imgUrl;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(BigDecimal originPrice) {
		this.originPrice = originPrice;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
	public String getSpecificationIds() {
		return specificationIds;
	}
	public void setSpecificationIds(String specificationIds) {
		this.specificationIds = specificationIds;
	}
	
}

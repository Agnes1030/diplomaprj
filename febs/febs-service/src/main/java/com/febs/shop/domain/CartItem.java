package com.febs.shop.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8810873108937641870L;
	private Long productId;
	private String productName;
	// 规格ID组合
	private String specificationIds;
	// 规格json字符
	private String specification;

	private BigDecimal price;
	private String imgUrl;
	private Integer quantity;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getSpecificationIds() {
		return specificationIds;
	}

	public void setSpecificationIds(String specificationIds) {
		this.specificationIds = specificationIds;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

}

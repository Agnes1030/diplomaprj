package com.febs.shop.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;
@Table(name = "t_product_category_mapping")
public class ProductCategoryMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7798755013108800267L;
	@Column(name = "product_id")
	private Long productId;
	@Column(name = "category_id")
	private Long categoryId;
   
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
}

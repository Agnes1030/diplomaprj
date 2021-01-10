package com.febs.shop.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "t_product_topic_mapping")
public class ProductTopicMapping implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6608097126381737328L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "topic_id")
	private Long topicId;
	@Column(name = "product_id")
	private Long productId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
}

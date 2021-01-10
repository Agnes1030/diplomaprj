package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 商品规格表值表 
 */
@Table(name = "t_product_specification_value")
public class ProductSpecificationValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2117715683171935271L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "product_id")
	private Long productId;
	// 规格ID(t_product_specification表主键)
	@Column(name="specification_id")
	private Long specificationId;
	// 规格值(一个规格ID对应多个规格值)
	@Column(name="spec_value")
	private String specValue;
	// 属性的先后顺序
	@Column(name="seq_num")
	private Integer seqNum;
	@Column(name = "create_time")
	private Date createTime;
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
	public Long getSpecificationId() {
		return specificationId;
	}
	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
	}
	public String getSpecValue() {
		return specValue;
	}
	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}

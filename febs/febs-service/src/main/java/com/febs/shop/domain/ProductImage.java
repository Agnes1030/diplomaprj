package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;
@Table(name = "t_product_image")
public class ProductImage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3790919940912314904L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "product_id")
	private Long productId;
	@Column(name = "src")
	private String src;
	@Column(name = "seq_num")
	private Integer seqNum;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
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
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
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

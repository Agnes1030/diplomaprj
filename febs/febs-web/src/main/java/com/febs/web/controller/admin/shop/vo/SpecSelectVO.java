package com.febs.web.controller.admin.shop.vo;

import java.io.Serializable;
// 商品规格视图对象
import java.util.List;

import com.febs.shop.domain.ProductSpecificationValue;
public class SpecSelectVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 792744890122531293L;
	private Long specId;
	private String specName;
	private List<ProductSpecificationValue> values;
	
	public Long getSpecId() {
		return specId;
	}
	public void setSpecId(Long specId) {
		this.specId = specId;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public List<ProductSpecificationValue> getValues() {
		return values;
	}
	public void setValues(List<ProductSpecificationValue> values) {
		this.values = values;
	}
	

}

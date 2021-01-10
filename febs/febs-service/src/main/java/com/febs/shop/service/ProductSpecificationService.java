package com.febs.shop.service;

import com.febs.common.service.IService;
import com.febs.shop.domain.ProductSpecification;

public interface ProductSpecificationService extends IService<ProductSpecification> {
	public int deleteByCondition(Object example);
	

}

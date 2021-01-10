package com.febs.shop.service;

import com.febs.common.service.IService;
import com.febs.shop.domain.ProductSpecificationValue;

public interface ProductSpecificationValueService extends IService<ProductSpecificationValue> {
	public int deleteByCondition(Object example);

}

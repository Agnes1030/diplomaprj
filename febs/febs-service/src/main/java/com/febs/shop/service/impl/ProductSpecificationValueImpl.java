package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.ProductSpecificationValueMapper;
import com.febs.shop.domain.ProductSpecificationValue;
import com.febs.shop.service.ProductSpecificationValueService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductSpecificationValueImpl extends BaseService<ProductSpecificationValue>
		implements ProductSpecificationValueService {
	@Autowired
	private ProductSpecificationValueMapper productSpecificationValueMapper;

	@Override
	public int deleteByCondition(Object example) {
		return productSpecificationValueMapper.deleteByExample(example);
	}

}

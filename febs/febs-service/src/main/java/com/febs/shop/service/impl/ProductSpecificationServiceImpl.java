package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.ProductSpecificationMapper;
import com.febs.shop.domain.ProductSpecification;
import com.febs.shop.service.ProductSpecificationService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductSpecificationServiceImpl extends BaseService<ProductSpecification>
		implements ProductSpecificationService {
	@Autowired
	private ProductSpecificationMapper productSpecificationMapper;

	@Override
	public int deleteByCondition(Object example) {
		return productSpecificationMapper.deleteByExample(example);
	}

}

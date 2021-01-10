package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.ProductSkuMapper;
import com.febs.shop.domain.ProductSku;
import com.febs.shop.service.ProductSkuService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductSkuServiceImpl extends BaseService<ProductSku> implements ProductSkuService {
	@Autowired
	private ProductSkuMapper productSkuMapper;

	@Override
	public int deleteByCondition(Object example) {
		return productSkuMapper.deleteByExample(example);
	}

	@Override
	public Integer updateStock(Long skuId, Integer num) {
		return productSkuMapper.updateStock(skuId, num);
	}

}

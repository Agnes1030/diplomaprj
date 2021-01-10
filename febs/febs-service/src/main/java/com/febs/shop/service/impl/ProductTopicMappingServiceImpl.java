package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.ProductTopicMappingMapper;
import com.febs.shop.domain.ProductTopicMapping;
import com.febs.shop.service.ProductTopicMappingService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductTopicMappingServiceImpl extends BaseService<ProductTopicMapping>
		implements ProductTopicMappingService {
	@Autowired
	private ProductTopicMappingMapper productTopicMappingMapper;

	@Override
	public Integer deleteTopicMapping(Example example) {
		return productTopicMappingMapper.deleteByExample(example);
	}

}

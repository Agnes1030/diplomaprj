package com.febs.shop.service;

import com.febs.common.service.IService;
import com.febs.shop.domain.ProductTopicMapping;

import tk.mybatis.mapper.entity.Example;

public interface ProductTopicMappingService extends IService<ProductTopicMapping> {
	public Integer deleteTopicMapping(Example example);

}

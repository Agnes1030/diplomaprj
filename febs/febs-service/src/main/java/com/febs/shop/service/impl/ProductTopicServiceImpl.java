package com.febs.shop.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.ProductTopicMapper;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductTopic;
import com.febs.shop.service.ProductTopicService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductTopicServiceImpl extends BaseService<ProductTopic> implements ProductTopicService {
	@Autowired
	private ProductTopicMapper productTopicMapper;


	@Override
	public List<Product> getTopicProducts(Long topicId) {
		return productTopicMapper.getTopicProducts(topicId);
	}

	@Override
	public ProductTopic findBySlug(String slug) {
		Example example = new Example(ProductTopic.class);
		example.createCriteria().andEqualTo("slug", slug);
		List<ProductTopic> topics = this.selectByExample(example);
		return topics.isEmpty() ? null : topics.get(0);
	}

	@Override
	public ProductTopic findByIdSlug(String idSlug) {
		if (StringUtils.isNumeric(idSlug)) {
			return this.productTopicMapper.selectByPrimaryKey(idSlug);
		} else {
			return this.findBySlug(idSlug);
		}
	}

}

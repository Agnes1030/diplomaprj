package com.febs.shop.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductTopic;

public interface ProductTopicService extends IService<ProductTopic> {
	public List<Product> getTopicProducts(Long topicId);
	public ProductTopic findBySlug(String slug);
	public ProductTopic findByIdSlug(String idSlug);

}

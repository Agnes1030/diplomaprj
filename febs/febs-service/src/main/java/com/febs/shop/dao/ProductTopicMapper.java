package com.febs.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import com.febs.common.config.MyMapper;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductTopic;

public interface ProductTopicMapper extends MyMapper<ProductTopic> {
	public static class TopicSqlProvider {
		public static String selectTopicProduct(Long topicId) {
			return "SELECT trp.* FROM t_product_topic_mapping tp left join t_product trp on tp.product_id=trp.id where tp.topic_id=#{topicId}";
		}
	}

	@SelectProvider(type = TopicSqlProvider.class, method = "selectTopicProduct")
	@ResultMap("productMap")
	public List<Product> getTopicProducts(Long topicId);
}

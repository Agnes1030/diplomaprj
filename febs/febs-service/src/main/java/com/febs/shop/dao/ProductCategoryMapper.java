package com.febs.shop.dao;

import java.util.List;

import com.febs.common.config.MyMapper;
import com.febs.shop.domain.ProductCategory;

public interface ProductCategoryMapper extends MyMapper<ProductCategory> {
	// 删除父节点，子节点变成顶级节点（根据实际业务调整）
	void changeToTop(List<String> categoryIds);
}

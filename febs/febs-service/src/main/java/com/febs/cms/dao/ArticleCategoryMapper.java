package com.febs.cms.dao;

import java.util.List;

import com.febs.cms.domain.ArticleCategory;
import com.febs.common.config.MyMapper;

public interface ArticleCategoryMapper extends MyMapper<ArticleCategory> {
	// 删除父节点，子节点变成顶级节点（根据实际业务调整）
	void changeToTop(List<String> categoryIds);
}

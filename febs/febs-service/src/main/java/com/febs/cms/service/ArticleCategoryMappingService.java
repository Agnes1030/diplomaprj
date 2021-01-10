package com.febs.cms.service;

import com.febs.cms.domain.ArticleCategoryMapping;
import com.febs.common.service.IService;

public interface ArticleCategoryMappingService extends IService<ArticleCategoryMapping> {
	public Integer deleteArticleCategoryMapping(Long articleId);
	public Integer batchDeleteCategoryMapping(String articleIds);
	public Integer deleteCategoryMappingByCat(String categoryIds);
}

package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.ArticleCategory;
import com.febs.common.domain.Tree;
import com.febs.common.service.IService;

public interface ArticleCategoryService extends IService<ArticleCategory> {
	List<ArticleCategory> getChildCategory(Long parentId);
	List<ArticleCategory> findCategoryByPid(Long parentId);
	Tree<ArticleCategory> getCategoryChildTree(ArticleCategory category);
	Tree<ArticleCategory> getCategoryTree(ArticleCategory category);

	List<ArticleCategory> findAllCategorys(ArticleCategory category);

	ArticleCategory findByName(String categoryName);

	ArticleCategory findBySlug(String slug);
	ArticleCategory findByIdSlug(String idSlug);

	ArticleCategory findById(Long categoryId);

	void addCategory(ArticleCategory category);

	void updateCategory(ArticleCategory category);

	void deleteCategorys(String categoryIds);

}

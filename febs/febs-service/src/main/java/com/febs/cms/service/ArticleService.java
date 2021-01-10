package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.Article;
import com.febs.common.service.IService;

public interface ArticleService extends IService<Article> {
	public Article findByIdSlug(String idSlug);

	public Article findBySlug(String slug);

	Article findById(Long articleId);

	public void updateArticle(Article article);

	public void addArticle(Article article);

	public void deleteArticles(String articleIds);

	public void updateViewCount(Long id);

	public List<Article> findUserArticles(Long userId);

	public List<Article> findAllArticles(Article article, List<Long> ids);

	public List<Article> findHotArticles(Long categoryId);
}

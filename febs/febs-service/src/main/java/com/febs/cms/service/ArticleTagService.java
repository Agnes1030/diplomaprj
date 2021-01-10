package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.ArticleTag;
import com.febs.common.service.IService;

public interface ArticleTagService extends IService<ArticleTag> {
	ArticleTag findById(Long articleTagId);
	public void updateArticleTag(ArticleTag articleTag);
	public void addArticleTag(ArticleTag articleTag);
	public void deleteArticleTags(String articleTagIds);
	public List<ArticleTag> getAllArticleTags(ArticleTag articleTag);
	public List<ArticleTag> getTagByName(String tagName);
	public List<ArticleTag> getTagByArticleIds(String articleIds);
}

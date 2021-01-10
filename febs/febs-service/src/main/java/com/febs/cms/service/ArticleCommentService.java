package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.ArticleComment;
import com.febs.common.service.IService;

public interface ArticleCommentService extends IService<ArticleComment> {
	public int addArticleComment(ArticleComment articleComment);
	public int updateArticleComment(ArticleComment articleComment);
	public ArticleComment findById(Long commentId);
	public List<ArticleComment> findAllArticleComments(ArticleComment articleComment);
	public void deleteArticleComments(String ids);
	public List<ArticleComment> selectArticleCommentsWithArticle(String content);
}

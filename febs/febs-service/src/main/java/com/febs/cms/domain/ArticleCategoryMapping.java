package com.febs.cms.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;
@Table(name = "t_article_category_mapping")
public class ArticleCategoryMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7798755013108800267L;
	@Column(name = "article_id")
	private Long articleId;
	@Column(name = "category_id")
	private Long categoryId;
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
}

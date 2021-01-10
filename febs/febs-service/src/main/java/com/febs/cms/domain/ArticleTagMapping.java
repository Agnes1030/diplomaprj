package com.febs.cms.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;
@Table(name = "t_article_tag_mapping")
public class ArticleTagMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1015701189072047951L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "article_id")
	@ExportConfig(value = "文章ID")
	private Long articleId;
	@Column(name = "tag_id")
	@ExportConfig(value = "标签ID")
	private Long tagId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	

}

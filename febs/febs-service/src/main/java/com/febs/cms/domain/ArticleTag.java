package com.febs.cms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_article_tag")
public class ArticleTag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4925835413661478992L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "tag_name")
	@ExportConfig(value = "tag名称")
	private String tagName;
	@Column(name = "template")
	@ExportConfig(value = "模板")
	private String template;
	@Column(name = "counts")
	@ExportConfig(value = "文章数量")
	private Integer counts;
	@Column(name = "CREATE_TIME")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}

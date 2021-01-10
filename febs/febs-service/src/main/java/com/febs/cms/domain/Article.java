package com.febs.cms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.febs.common.annotation.ExportConfig;
@Table(name = "t_article")
public class Article implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7836351084381251155L;

	public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";
	/**
	 * 
	 */
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "category_ids")
	private String categoryIds;
	@Column(name = "title")
	@ExportConfig(value = "标题")
	private String title;
	@Column(name = "slug")
	@ExportConfig(value = "slug")
	private String slug;	
	@Column(name = "tags")
	@ExportConfig(value = "tags")
	private String tags;
	@Column(name = "template")
	@ExportConfig(value = "模板")
	private String template;
	@Column(name = "meta_description")
	@ExportConfig(value = "描述")
	private String description;
	@Column(name = "meta_keywords")
	@ExportConfig(value = "关键字")
	private String keywords;
	@Column(name = "user_id")
	private Long userId;	
	@Column(name = "author")
	@ExportConfig(value = "作者")
	private String author;
	@Column(name = "seq_num")
	private Integer seqNum;
	@Column(name = "summary")
	@ExportConfig(value = "摘要")
	private String summary;	
	@Column(name = "thumbnail")
	@ExportConfig(value = "缩略图")
	private String thumbnail;	
	@Column(name = "content")
	@ExportConfig(value = "内容")
	private String content;
	@Column(name = "view_count")
	@ExportConfig(value = "阅读数量")
	private Integer viewCount;
    @ExportConfig(value = "状态", convert = "s:0=锁定,1=有效")
    @Column(name = "article_status")
    private String status = STATUS_VALID;
	@Column(name = "CREATE_TIME")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
  
	public String getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLinkUrl() {
		if(StringUtils.isBlank(this.getSlug())) {
            return "/article/view/"+this.id;			
		}else {
			return "/article/view/"+this.slug;			
		}
	}
	
}

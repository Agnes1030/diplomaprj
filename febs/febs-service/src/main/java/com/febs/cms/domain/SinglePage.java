package com.febs.cms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;
@Table(name = "t_single_page")
public class SinglePage implements Serializable {
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "title")
	@ExportConfig(value = "标题")
	private String title;
	@Column(name = "template")
	@ExportConfig(value = "模板")
	private String template;
	@Column(name = "description")
	@ExportConfig(value = "描述")
	private String description;
	@Column(name = "keywords")
	@ExportConfig(value = "关键字")
	private String keywords;
	@Column(name = "author")
	@ExportConfig(value = "作者")
	private String author;
	@Column(name = "seq_num")
	private Integer seqNum;
	@Column(name = "slug")
	@ExportConfig(value = "slug名称")
	private String slug;	
	@Column(name = "summary")
	@ExportConfig(value = "摘要")
	private String summary;		
	@Column(name = "content")
	@ExportConfig(value = "内容")
	private String content;
    @ExportConfig(value = "状态", convert = "s:0=锁定,1=有效")
    @Column(name = "page_status")
    private String status = STATUS_VALID;
    @ExportConfig(value = "是否显示在导航", convert = "s:0=不显示,1=显示")
    @Column(name = "nav_show")
    private String navShow = STATUS_VALID;
	@Column(name = "CREATE_TIME")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getNavShow() {
		return navShow;
	}
	public void setNavShow(String navShow) {
		this.navShow = navShow;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	
}

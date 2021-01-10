package com.febs.cms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_category")
public class ArticleCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "parent_id")
	private Long parentId;
	@Column(name = "name")
	@ExportConfig(value = "分类名称")
	private String name;
	@Column(name = "slug")
	@ExportConfig(value = "slug")
	private String slug;	
	@Column(name = "seq_num")
	private Integer seqNum;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "meta_keywords")
	@ExportConfig(value = "关键字")
	private String keywords;
	@Column(name = "meta_description")
	@ExportConfig(value = "描述")
	private String description;
	@Column(name = "counts")
	@ExportConfig(value = "文章数量")
	private Long counts;
	@Column(name = "template")
	@ExportConfig(value = "对应模板")
	private String template;
    @ExportConfig(value = "状态", convert = "s:0=锁定,1=有效")
    @Column(name = "category_status")
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getCounts() {
		return counts;
	}
	public void setCounts(Long counts) {
		this.counts = counts;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getNavShow() {
		return navShow;
	}
	public void setNavShow(String navShow) {
		this.navShow = navShow;
	}
	public String getLinkUrl() {
		if(StringUtils.isBlank(this.getSlug())) {
            return "/articleCategory/"+this.id;			
		}else {
			return "/articleCategory/"+this.slug;			
		}
	}
}

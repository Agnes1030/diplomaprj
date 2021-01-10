package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专题
 */
@Table(name = "t_product_topic")
public class ProductTopic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7106913606034954803L;
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "topic_name")
	private String name;
	@Column(name = "slug")
	private String slug;
	@Column(name = "seo_keywords")
	private String seoKeywords;
	@Column(name = "seo_descriptions")
	private String seoDescriptions;
	@Column(name = "descriptions")
	private String descriptions;
	@Column(name = "thumbnail")
	private String thumbnail;
	@Column(name = "recommend")
	private String recommend;
	@Column(name = "template")
	private String template;
	@Column(name="detail_template")
	private String detailTemplate;
    @Column(name = "nav_show")
    private String navShow = STATUS_VALID;
	@Column(name = "create_time")
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
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSeoKeywords() {
		return seoKeywords;
	}
	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}
	public String getSeoDescriptions() {
		return seoDescriptions;
	}
	public void setSeoDescriptions(String seoDescriptions) {
		this.seoDescriptions = seoDescriptions;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getNavShow() {
		return navShow;
	}
	public void setNavShow(String navShow) {
		this.navShow = navShow;
	}
	public String getDetailTemplate() {
		return detailTemplate;
	}
	public void setDetailTemplate(String detailTemplate) {
		this.detailTemplate = detailTemplate;
	}
   
}

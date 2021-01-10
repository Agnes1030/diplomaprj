package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_user_favorite")
public class Favorite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7921425912716617915L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	// 收藏用户
	@Column(name = "user_id")
	private Long userId;
	// 收藏数据的类型
	@Column(name = "type")
	private String type;
	@Column(name = "type_text")
	private String type_text;
	@Column(name = "type_id")
	private Long typeId;
	@Column(name = "title")
	private String title;
	// 摘要
	@Column(name = "summary")
	private String summary;
	// 缩略图
	@Column(name = "thumbnail")
	private String thumbnail;
	// 详情页
	@Column(name = "link")
	private String link;
	@Column(name = "options")
	private String options;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType_text() {
		return type_text;
	}
	public void setType_text(String type_text) {
		this.type_text = type_text;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}

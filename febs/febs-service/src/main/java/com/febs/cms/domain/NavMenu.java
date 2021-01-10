package com.febs.cms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_nav_menu")
public class NavMenu implements Serializable {
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
	@ExportConfig(value = "导航名称")
	private String name;
	@Column(name = "target")
	private String target;
	@Column(name = "seq_num")
	private Integer seqNum;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "nav_url")
	@ExportConfig(value = "url访问路径")
	private String navUrl;
	@Column(name = "nav_type")
	@ExportConfig(value = "内容类型")
	private String navType;
	@Column(name = "relation_id")
	@ExportConfig(value = "内容id")
	private Long relationId;	
	@ExportConfig(value = "状态", convert = "s:0=锁定,1=有效")
	@Column(name = "nav_status")
	private String status;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getNavType() {
		return navType;
	}

	public void setNavType(String navType) {
		this.navType = navType;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
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

	public String getNavUrl() {
		return navUrl;
	}

	public void setNavUrl(String navUrl) {
		this.navUrl = navUrl;
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}

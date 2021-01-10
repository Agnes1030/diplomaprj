package com.febs.cms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_banner")
public class Banner implements Serializable {
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
	@Column(name = "img_path")
	@ExportConfig(value = "图片路径")
	private String imgPath;
	@Column(name = "open_url")
	@ExportConfig(value = "打开链接")
	private String openUrl;
	@Column(name = "open_target")
	@ExportConfig(value = "打开方式")
	private String openTarget;
	@Column(name = "seq_num")
	private Integer seqNum;
	@Column(name = "description")
	@ExportConfig(value = "描述")
	private String description;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	public String getOpenTarget() {
		return openTarget;
	}

	public void setOpenTarget(String openTarget) {
		this.openTarget = openTarget;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

}

package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;
@Table(name = "t_user_order_invoice")
public class OrderInvoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4841025342189182362L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	@Column(name = "type")
	@ExportConfig(value = "发票类型")
	private String type;
	@Column(name = "title")
	@ExportConfig(value = "发票抬头")
	private String title;
	@Column(name = "content")
	@ExportConfig(value = "发票内容")
	private String content;
	@Column(name = "identity")
	@ExportConfig(value = "纳税人识别号")
	private String identity;
	@Column(name = "name")
	@ExportConfig(value = "单位名称")
	private String name;
	@Column(name = "mobile")
	@ExportConfig(value = "发票收取人手机号")
	private String mobile;
	@Column(name = "email")
	@ExportConfig(value = "发票收取人邮箱")
	private String email;
	@Column(name = "invoice_status")
	@ExportConfig(value = "发票状态")
	private Integer invoiceStatus;
	// json字段扩展
	@Column(name = "options")
	private String options;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	@Column(name = "modified_time")
	@ExportConfig(value = "最后更新时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date modifiedTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
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
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
}

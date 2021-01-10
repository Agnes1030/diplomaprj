package com.febs.shop.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.febs.common.annotation.ExportConfig;

/**
 * 优惠券类型
 */
@Table(name = "t_coupon")
public class Coupon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4779192881665278102L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;
	// 标题
	@Column(name = "title")
	private String title;
	// 图标
	@Column(name = "icon")
	private String icon;
	// 1满减券 2叠加满减券 3无门槛券
	@Column(name = "type")
	private Integer type;
	// 满多少金额
	@Column(name = "with_amount")
	private BigDecimal withAmount;
	// 是否是推广奖励券
	@Column(name = "with_award")
	private Boolean withAward;
	// 是不是只有领取人可用，如果不是，领取人可以随便给其他人用
	@Column(name = "with_owner")
	private Boolean withOwner;
	// 优惠券金额
	@Column(name = "amount")
	private BigDecimal amount;

	// 配额：配发数量
	@Column(name = "quota")
	private Integer quota;
	// 已领取数量
	@Column(name = "take_count")
	private Integer takeCount;
	// 已使用数量
	@Column(name = "used_count")
	private Integer usedCount;
	@Column(name = "start_time")
	private Date startTime;
	// 发放结束时间
	@Column(name = "end_time")
	private Date endTime;
	// 时效:1绝对时效（XXX-XXX时间段有效） 2相对时效（领取后N天有效）
	@Column(name = "valid_type")
	private Integer validType;
	// 使用开始时间
	@Column(name = "valid_start_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date validStartTime;
	// 使用结束时间
	@Column(name = "valid_end_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date validEndTime;
	// 自领取之日起有效天数
	@Column(name = "valid_days")
	private Integer validDays;
	// 商品限制类型，如果0则全商品，如果是1则是类目限制，如果是2则是商品限制。
	@Column(name = "products_type")
	private Integer productsType;
	// 商品限制值，productsType如果是0则空集合，如果是1则是类目集合，如果是2则是商品集合。
	@Column(name = "products_value")
	private String productsValue;
	// 创建用户ID
	@Column(name = "create_user_id")
	private Long createUserId;
	// 兑换码(可选，如果有值说明支持兑换码领取此类型优惠券)
	@Column(name = "code")
	private String code;
	// 状态 0锁定 1有效
	@Column(name = "cou_status")
	private Integer couStatus;
	@Column(name = "options")
	private String options;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	@Column(name = "modified_time")
	@ExportConfig(value = "修改时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date modifiedTime;

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getWithAmount() {
		return withAmount;
	}

	public void setWithAmount(BigDecimal withAmount) {
		this.withAmount = withAmount;
	}

	public Boolean getWithAward() {
		return withAward;
	}

	public void setWithAward(Boolean withAward) {
		this.withAward = withAward;
	}

	public Boolean getWithOwner() {
		return withOwner;
	}

	public void setWithOwner(Boolean withOwner) {
		this.withOwner = withOwner;
	}
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}

	public Integer getTakeCount() {
		return takeCount;
	}

	public void setTakeCount(Integer takeCount) {
		this.takeCount = takeCount;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getValidType() {
		return validType;
	}

	public void setValidType(Integer validType) {
		this.validType = validType;
	}

	public Date getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Date validStartTime) {
		this.validStartTime = validStartTime;
	}

	public Date getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getCouStatus() {
		return couStatus;
	}

	public void setCouStatus(Integer couStatus) {
		this.couStatus = couStatus;
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

	public Integer getProductsType() {
		return productsType;
	}

	public void setProductsType(Integer productsType) {
		this.productsType = productsType;
	}

	public String getProductsValue() {
		return productsValue;
	}

	public void setProductsValue(String productsValue) {
		this.productsValue = productsValue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

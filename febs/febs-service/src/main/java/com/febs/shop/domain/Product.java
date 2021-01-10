package com.febs.shop.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_product")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2789456292111273935L;
	public static final String STATUS_VALID = "1";

	public static final String STATUS_LOCK = "0";
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
	@Column(name = "usp")
	@ExportConfig(value = "产品卖点")
	private String usp;
	@Column(name = "video")
	@ExportConfig(value = "视频")
	private String video;
	@Column(name = "video_cover")
	@ExportConfig(value = "video_cover")
	private String videoCover;
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
	@Column(name = "price")
	private BigDecimal price;
	@Column(name = "origin_price")
	@ExportConfig(value = "原始价格")
	private BigDecimal originPrice;
	@Column(name = "limited_price")
	@ExportConfig(value = "限时优惠价格")
	private BigDecimal limitedPrice;
	@Column(name = "limited_time")
	@ExportConfig(value = "限时优惠时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date limitedTime;
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
	@Column(name = "product_status")
	private String status = STATUS_VALID;
	@ExportConfig(value = "是否允许评论", convert = "s:0=否,1=是")
	@Column(name = "comment_status")
	private String commentStatus = STATUS_VALID;
	@Column(name = "comment_count")
	@ExportConfig(value = "评论数量")
	private Integer commentCount;
	@Column(name = "sales_count")
	@ExportConfig(value = "销量")
	private Integer salesCount;
	@Column(name = "create_time")
	@ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date createTime;
	@Column(name = "modified_time")
	@ExportConfig(value = "最后更新时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
	private Date modifiedTime;
	@Column(name = "remarks")
	@ExportConfig(value = "备注信息")
	private String remarks;
	@Column(name = "options")
	@ExportConfig(value = "options")
	private String options;
	private List<ProductImage> productImages;
	
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
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public String getUsp() {
		return usp;
	}
	public void setUsp(String usp) {
		this.usp = usp;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getVideoCover() {
		return videoCover;
	}
	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(BigDecimal originPrice) {
		this.originPrice = originPrice;
	}
	public BigDecimal getLimitedPrice() {
		return limitedPrice;
	}
	public void setLimitedPrice(BigDecimal limitedPrice) {
		this.limitedPrice = limitedPrice;
	}
	public Date getLimitedTime() {
		return limitedTime;
	}
	public void setLimitedTime(Date limitedTime) {
		this.limitedTime = limitedTime;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCommentStatus() {
		return commentStatus;
	}
	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}
	public Integer getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public List<ProductImage> getProductImages() {
		return productImages;
	}
	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}
	public String getLinkUrl() {
		if(StringUtils.isBlank(this.getSlug())) {
            return "/product/view/"+this.id;			
		}else {
			return "/product/view/"+this.slug;			
		}
	}
}

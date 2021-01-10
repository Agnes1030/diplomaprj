package com.febs.shop.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_product_comment")
public class ProductComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4526666658241269655L;
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
	@Column(name = "parent_id")
	private Long parentId;
	@Column(name = "product_id")
	private Long productId;
	@Column(name = "title")
	private String title;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "author")
	@ExportConfig(value = "作者")
	private String author;
	@Column(name = "modify_uid")
	private Long modifyUid;
	@Column(name = "modify_username")
	private String modifyUserName;
	@Column(name = "modify_time")
	private Date modifyTime;
	@Column(name = "content")
	@ExportConfig(value = "评论内容")
	private String content;
	@Column(name = "reply_count")
	private Long replyCount;
	@Column(name = "seq_num")
	private Long seqNum;
	// 顶数量
	@Column(name = "vote_up")
	private Long voteUp;
	// 踩数量
	@Column(name = "vote_down")
	private Long voteDown;
	@ExportConfig(value = "状态", convert = "s:0=锁定,1=有效")
	@Column(name = "comment_status")
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

	

	public Long getModifyUid() {
		return modifyUid;
	}

	public void setModifyUid(Long modifyUid) {
		this.modifyUid = modifyUid;
	}

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Long getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Long replyCount) {
		this.replyCount = replyCount;
	}

	public Long getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
	}

	public Long getVoteUp() {
		return voteUp;
	}

	public void setVoteUp(Long voteUp) {
		this.voteUp = voteUp;
	}

	public Long getVoteDown() {
		return voteDown;
	}

	public void setVoteDown(Long voteDown) {
		this.voteDown = voteDown;
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}

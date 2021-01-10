package com.febs.member.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 用户收藏 
 */
@Table(name = "t_user_favourite")
public class UserFavourite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1485939094243928150L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	//所收藏的内容的主键ID
	@Column(name = "favourite_id")
	private Long favouriteId;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "title")
	private String title;
	@Column(name = "type")
	private String type;
	@Column(name = "create_time")
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getFavouriteId() {
		return favouriteId;
	}
	public void setFavouriteId(Long favouriteId) {
		this.favouriteId = favouriteId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}

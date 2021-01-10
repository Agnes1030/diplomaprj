package com.febs.system.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.febs.common.annotation.ExportConfig;

@Table(name = "t_user")
public class MyUser implements Serializable {

    private static final long serialVersionUID = -4852732617765810959L;
    /**
     * 账户状态
     */
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";

    public static final String DEFAULT_THEME = "green";

    public static final String DEFAULT_AVATAR = "default.jpg";

    /**
     * 性别
     */
    public static final String SEX_MALE = "0";

    public static final String SEX_FEMALE = "1";

    public static final String SEX_UNKNOW = "2";

    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "user_id")
    private Long userId;

    @ExportConfig(value = "用户名")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    // 余额
    @Column(name="amount")
    private BigDecimal amount;
    @Column(name = "dept_id")
    private Long deptId;

    @ExportConfig(value = "部门")
    @Transient
    private String deptName;

    @ExportConfig(value = "邮箱")
    @Column(name = "email")
    private String email;

    @ExportConfig(value = "手机")
    @Column(name = "mobile")
    private String mobile;

    @ExportConfig(value = "状态", convert = "s:0=锁定,1=有效")
    @Column(name = "status")
    private String status;

    @ExportConfig(value = "创建时间", convert = "c:com.febs.common.utils.poi.convert.TimeConvert")
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @ExportConfig(value = "性别", convert = "s:0=男,1=女,2=保密")
    @Column(name = "ssex")
    private String ssex;

    @ExportConfig(value = "主题")
    @Column(name = "theme")
    private String theme;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "description")
    private String description;

    @Transient
    private String roleName;


    /**
     * @return USER_ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return USERNAME
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * @return PASSWORD
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * @return DEPT_ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * @return EMAIL
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * @return MOBILE
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }


    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
     * @return MODIFY_TIME
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return LAST_LOGIN_TIME
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * @return SSEX
     */
    public String getSsex() {
        return ssex;
    }

    /**
     * @param ssex
     */
    public void setSsex(String ssex) {
        this.ssex = ssex == null ? null : ssex.trim();
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
    public String toString() {
        return "MyUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", lastLoginTime=" + lastLoginTime +
                ", ssex='" + ssex + '\'' +
                ", theme='" + theme + '\'' +
                ", avatar='" + avatar + '\'' +
                ", description='" + description + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MyUser && StringUtils.equals(this.username, ((MyUser) o).username);
    }

    @Override
    public int hashCode() {

        return this.username.hashCode();
    }
}
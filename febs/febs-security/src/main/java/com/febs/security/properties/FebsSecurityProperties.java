package com.febs.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "febs.security")
public class FebsSecurityProperties {
	// admin主页 URL
	private String adminIndex;
	// member主页 URL
	private String memberIndex;
	private String adminUrl;
	private String memberUrl;
	// 登录 URL
	private String adminLogin;
	private String memberLogin;
	// 免认证静态资源路径
	private String anonResourcesUrl;
	// 记住我超时时间
	private int rememberMeTimeout;
	// 登出 URL
	private String logoutUrl;
	private String invalidSessionUrl;
	// 哪些角色可以访问管理后台例如: admin,manager
	private String adminRoles;

	private ValidateCodeProperties code = new ValidateCodeProperties();

	private SocialProperties social = new SocialProperties();

	private SessionProperties session = new SessionProperties();

	public String getAnonResourcesUrl() {
		return anonResourcesUrl;
	}

	public void setAnonResourcesUrl(String anonResourcesUrl) {
		this.anonResourcesUrl = anonResourcesUrl;
	}

	public ValidateCodeProperties getCode() {
		return code;
	}

	public void setCode(ValidateCodeProperties code) {
		this.code = code;
	}

	public String getAdminRoles() {
		return adminRoles;
	}

	public void setAdminRoles(String adminRoles) {
		this.adminRoles = adminRoles;
	}

	public int getRememberMeTimeout() {
		return rememberMeTimeout;
	}

	public void setRememberMeTimeout(int rememberMeTimeout) {
		this.rememberMeTimeout = rememberMeTimeout;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public SocialProperties getSocial() {
		return social;
	}

	public void setSocial(SocialProperties social) {
		this.social = social;
	}

	public SessionProperties getSession() {
		return session;
	}

	public void setSession(SessionProperties session) {
		this.session = session;
	}

	public String getAdminUrl() {
		return adminUrl;
	}

	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}

	public String getMemberUrl() {
		return memberUrl;
	}

	public void setMemberUrl(String memberUrl) {
		this.memberUrl = memberUrl;
	}

	public String getAdminLogin() {
		return adminLogin;
	}

	public void setAdminLogin(String adminLogin) {
		this.adminLogin = adminLogin;
	}

	public String getMemberLogin() {
		return memberLogin;
	}

	public void setMemberLogin(String memberLogin) {
		this.memberLogin = memberLogin;
	}

	public String getAdminIndex() {
		return adminIndex;
	}

	public void setAdminIndex(String adminIndex) {
		this.adminIndex = adminIndex;
	}

	public String getMemberIndex() {
		return memberIndex;
	}

	public void setMemberIndex(String memberIndex) {
		this.memberIndex = memberIndex;
	}

	public String getInvalidSessionUrl() {
		return invalidSessionUrl;
	}

	public void setInvalidSessionUrl(String invalidSessionUrl) {
		this.invalidSessionUrl = invalidSessionUrl;
	}

}

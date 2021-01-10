package com.febs.security.service;

import static com.febs.common.utils.FebsUtil.isPhoneNo;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

import com.febs.common.utils.DateUtil;
import com.febs.security.domain.FebsSocialUserDetails;
import com.febs.security.domain.FebsUserDetails;
import com.febs.security.domain.LoginType;
import com.febs.security.exception.FebsCredentialExcetion;
import com.febs.system.domain.MyUser;
import com.febs.system.service.MenuService;
import com.febs.system.service.RoleService;
import com.febs.system.service.UserService;

@Configuration
public class FebsUserDetailService implements UserDetailsService, SocialUserDetailsService {

	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		MyUser user = this.userService.findByNameOrMobile(username);
		boolean isMobile = isPhoneNo(username);
		if (user != null) {
			String permissions = this.menuService.findUserPermissions(user.getUsername());
			String roles = this.roleService.findUserRoles(user.getUsername());
			boolean notLocked = false;
			if (StringUtils.equals(MyUser.STATUS_VALID, user.getStatus()))
				notLocked = true;
			FebsUserDetails userDetails = new FebsUserDetails(user.getUsername(), user.getPassword(), true, true, true,
					notLocked, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions + "," + roles));
			userDetails.setTheme(user.getTheme());
			userDetails.setAvatar(user.getAvatar());
			userDetails.setEmail(user.getEmail());
			userDetails.setMobile(user.getMobile());
			userDetails.setSsex(user.getSsex());
			userDetails.setUserId(user.getUserId());
			userDetails.setPassword(user.getPassword());
			userDetails.setLoginTime(DateUtil.getDateFormat(new Date(), DateUtil.FULL_DATE_FORMAT));
			if (isMobile)
				userDetails.setLoginType(LoginType.sms);
			return userDetails;
		} else {
			if (isMobile)
				throw new FebsCredentialExcetion("该手机号还未绑定账号！");
			throw new UsernameNotFoundException("");
		}

	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) {
		MyUser user = this.userService.findByNameOrMobile(userId);
		if (user != null) {
			String permissions = this.menuService.findUserPermissions(user.getUsername());
			boolean notLocked = false;
			if (StringUtils.equals(MyUser.STATUS_VALID, user.getStatus()))
				notLocked = true;
			FebsSocialUserDetails userDetails = new FebsSocialUserDetails(user.getUsername(), user.getPassword(), true,
					true, true, notLocked, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
			userDetails.setTheme(user.getTheme());
			userDetails.setAvatar(user.getAvatar());
			userDetails.setEmail(user.getEmail());
			userDetails.setMobile(user.getMobile());
			userDetails.setSsex(user.getSsex());
			userDetails.setUsersId(user.getUserId());
			userDetails.setPassword(user.getPassword());
			return userDetails;
		} else {
			return null;
		}
	}
}

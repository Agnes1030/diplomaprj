package com.febs.system.service;

import org.springframework.cache.annotation.CacheConfig;

import com.febs.common.service.IService;
import com.febs.system.domain.UserRole;
@CacheConfig
public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String roleIds);
    
	void deleteUserRolesByUserId(String userIds);
}

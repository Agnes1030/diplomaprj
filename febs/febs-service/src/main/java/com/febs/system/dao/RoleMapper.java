package com.febs.system.dao;

import java.util.List;

import com.febs.common.config.MyMapper;
import com.febs.system.domain.Role;
import com.febs.system.domain.RoleWithMenu;

public interface RoleMapper extends MyMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
	List<RoleWithMenu> findById(Long roleId);
}
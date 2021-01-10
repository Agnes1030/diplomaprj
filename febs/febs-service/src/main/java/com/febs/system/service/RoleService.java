package com.febs.system.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.system.domain.Role;
import com.febs.system.domain.RoleWithMenu;

public interface RoleService extends IService<Role> {
    String findUserRoles(String userName);

	List<Role> findUserRole(String userName);

	List<Role> findAllRole(Role role);
	
	RoleWithMenu findRoleWithMenus(Long roleId);

	Role findByName(String roleName);

	void addRole(Role role, Long[] menuIds);
	
	void updateRole(Role role, Long[] menuIds);

	void deleteRoles(String roleIds);
}

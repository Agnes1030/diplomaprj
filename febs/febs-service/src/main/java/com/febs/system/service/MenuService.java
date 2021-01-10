package com.febs.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.febs.common.domain.Tree;
import com.febs.common.service.IService;
import com.febs.system.domain.Menu;

@CacheConfig(cacheNames = "MenuService")
public interface MenuService extends IService<Menu> {

	String findUserPermissions(String userName);

	List<Menu> findUserMenus(String userName);

	List<Menu> findAllMenus(Menu menu);

	Tree<Menu> getMenuButtonTree();
	Tree<Menu> getMenuTree();

	Tree<Menu> getUserMenu(String userName);

	Menu findById(Long menuId);

	Menu findByNameAndType(String menuName, String type);

	// 菜单有更新或者删除 ，都要删除menuTree重新查询计算 key如果是纯字符串用单引号标准
	void addMenu(Menu menu);

	void updateMenu(Menu menu);

	void deleteMeuns(String menuIds);

	/**
	 * 此缓存不需要刷新，因为是直接读取的程序注解。因为新增方法注解，程序会重启。应该会每次读取最新注解,但如果重启时redis没更新，可能会有部分缓存未更新。这方法只是辅助作用，影响不大
	 */
	@Cacheable(key = "'url_'+ #p0")
	List<Map<String, String>> getAllUrl(String p1);

}

package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.NavMenu;
import com.febs.common.domain.Tree;
import com.febs.common.service.IService;

public interface NavMenuService extends IService<NavMenu> {
	public NavMenu findNavByRelation(String navType, Long relationId);

	Tree<NavMenu> getNavMenuTree();

	List<NavMenu> findAllNavMenus(NavMenu navMenu);

	List<NavMenu> findChildNavMenus(Long navMenuId);

	NavMenu findByName(String navMenuName);

	NavMenu findById(Long navMenuId);

	void addNavMenu(NavMenu navMenu);

	void updateNavMenu(NavMenu navMenu);

	void deleteNavMenus(String navMenuIds);

	void refreshNavs();

	void setActiveNav(String menuId);

	NavMenu findByPyiyinName(String pinyinName);

	void deleteNav(NavMenu navMenu);

	void deleteNavByCategoryIds(String categoryIds);

	void deleteNavByProductCategoryIds(String categoryIds);

	void deleteNavByTopicIds(String topicIds);

}

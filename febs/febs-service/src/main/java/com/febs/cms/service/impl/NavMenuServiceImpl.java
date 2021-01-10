package com.febs.cms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.cms.dao.NavMenuMapper;
import com.febs.cms.domain.NavMenu;
import com.febs.cms.service.NavMenuService;
import com.febs.common.domain.Tree;
import com.febs.common.service.impl.BaseService;
import com.febs.common.utils.TreeUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("navMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class NavMenuServiceImpl extends BaseService<NavMenu> implements NavMenuService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	NavMenuMapper navMenuMapper;
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;

	@Override
	public void refreshNavs() {
		if (thymeleafViewResolver != null) {
			Tree<NavMenu> tree = this.getNavMenuTree();
			Map<String, Object> vars = new HashMap<>(1);
			vars.put("navs", tree);
			thymeleafViewResolver.setStaticVariables(vars);
		}
	}

	@Override
	public Tree<NavMenu> getNavMenuTree() {
		List<Tree<NavMenu>> trees = new ArrayList<>();
		NavMenu menu = new NavMenu();
		menu.setStatus(NavMenu.STATUS_VALID);
		List<NavMenu> navMenus = this.findAllNavMenus(menu);
		navMenus.forEach(navMenu -> {
			Tree<NavMenu> tree = new Tree<>();
			tree.setId(navMenu.getId().toString());
			tree.setParentId(navMenu.getParentId().toString());
			tree.setText(navMenu.getName());
			tree.setUrl(navMenu.getNavUrl());
			trees.add(tree);
		});
		return TreeUtils.build(trees);
	}

	@Override
	public List<NavMenu> findAllNavMenus(NavMenu navMenu) {
		try {
			Example example = new Example(NavMenu.class);
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(navMenu.getName())) {
				criteria.andCondition("name like", "%" + navMenu.getName() + "%");
			}
			if (StringUtils.isNoneBlank(navMenu.getNavType())) {
				criteria.andEqualTo("navType", navMenu.getNavType());
			}
			if (null != navMenu.getRelationId() && navMenu.getRelationId() > 0) {
				criteria.andEqualTo("relationId", navMenu.getRelationId());
			}
			if (StringUtils.isNotBlank(navMenu.getStatus())) {
				criteria.andEqualTo("status", navMenu.getStatus());
			}
			example.setOrderByClause("seq_num");
			return this.selectByExample(example);
		} catch (Exception e) {
			log.error("获取分类列表失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	public NavMenu findByName(String navMenuName) {
		Example example = new Example(NavMenu.class);
		example.createCriteria().andCondition("lower(name) =", navMenuName.toLowerCase());
		List<NavMenu> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public NavMenu findByPyiyinName(String pinyinName) {
		Example example = new Example(NavMenu.class);
		example.createCriteria().andEqualTo("pinyinName", pinyinName);
		List<NavMenu> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public NavMenu findById(Long navMenuId) {
		return this.selectByKey(navMenuId);
	}

	@Override
	@Transactional
	public void addNavMenu(NavMenu navMenu) {
		Long parentId = navMenu.getParentId();
		if (parentId == null)
			navMenu.setParentId(0L);
		navMenu.setCreateTime(new Date());
		this.save(navMenu);
		this.refreshNavs();
	}

	@Override
	@Transactional
	public void updateNavMenu(NavMenu navMenu) {
		this.updateNotNull(navMenu);
		this.refreshNavs();
	}

	@Override
	@Transactional
	public void deleteNavMenus(String navIds) {
		List<String> list = Arrays.asList(navIds.split(","));
		this.batchDelete(list, "id", NavMenu.class);
		this.navMenuMapper.changeToTop(list);
		this.refreshNavs();

	}

	@Override
	public void setActiveNav(String menuId) {
		@SuppressWarnings("unchecked")
		Tree<NavMenu> tree = (Tree<NavMenu>) thymeleafViewResolver.getStaticVariables().get("navs");
		TreeUtils.setActiveNode(tree.getChildren(), menuId);
	}

	@Override
	public void deleteNav(NavMenu navMenu) {
		Example example = new Example(NavMenu.class);
		example.createCriteria().andEqualTo("navType", navMenu.getNavType()).andEqualTo("relationId",
				navMenu.getRelationId());
		this.getMapper().deleteByExample(example);
		this.refreshNavs();
	}

	@Override
	public void deleteNavByCategoryIds(String categoryIds) {
		List<String> list = Arrays.asList(categoryIds.split(","));
		Example example = new Example(NavMenu.class);
		example.createCriteria().andIn("relationId", list).andEqualTo("navType", "category");
		this.getMapper().deleteByExample(example);
		this.refreshNavs();
	}

	@Override
	public void deleteNavByProductCategoryIds(String categoryIds) {
		List<String> list = Arrays.asList(categoryIds.split(","));
		Example example = new Example(NavMenu.class);
		example.createCriteria().andIn("relationId", list).andEqualTo("navType", "productCategory");
		this.getMapper().deleteByExample(example);
		this.refreshNavs();

	}

	@Override
	public NavMenu findNavByRelation(String navType, Long relationId) {
		Example example = new Example(NavMenu.class);
		example.createCriteria().andEqualTo("navType", navType);
		example.createCriteria().andEqualTo("relationId", relationId);
		List<NavMenu> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public void deleteNavByTopicIds(String topicIds) {
		List<String> list = Arrays.asList(topicIds.split(","));
		Example example = new Example(NavMenu.class);
		example.createCriteria().andIn("relationId", list).andEqualTo("navType", "topic");
		this.getMapper().deleteByExample(example);
		this.refreshNavs();
	}

	@Override
	public List<NavMenu> findChildNavMenus(Long navMenuId) {
		Example example = new Example(NavMenu.class);
		example.createCriteria().andEqualTo("parentId", navMenuId);
		return this.selectByExample(example);
	}

}

package com.febs.cms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.cms.dao.ArticleCategoryMapper;
import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.common.domain.Tree;
import com.febs.common.service.impl.BaseService;
import com.febs.common.utils.PinyinUtil;
import com.febs.common.utils.TreeUtils;

import tk.mybatis.mapper.entity.Example;

@Service("categoryService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ArticleCategoryServiceImpl extends BaseService<ArticleCategory> implements ArticleCategoryService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	ArticleCategoryMapper categoryMapper;
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;

	@Override
	public Tree<ArticleCategory> getCategoryTree(ArticleCategory category) {
		List<Tree<ArticleCategory>> trees = new ArrayList<>();
		List<ArticleCategory> categorys = this.findAllCategorys(category);
		categorys.forEach(cat -> {
			Tree<ArticleCategory> tree = new Tree<ArticleCategory>();
			tree.setId(cat.getId().toString());
			tree.setParentId(cat.getParentId().toString());
			tree.setText(cat.getName());
			trees.add(tree);
		});
		return TreeUtils.build(trees);
	}

	@Override
	public List<ArticleCategory> findAllCategorys(ArticleCategory category) {
		try {
			Example example = new Example(ArticleCategory.class);
			if (null != category && StringUtils.isNotBlank(category.getName())) {
				example.createCriteria().andCondition("name like", "%" + category.getName() + "%");
			}
			example.setOrderByClause("seq_num");
			return this.selectByExample(example);
		} catch (Exception e) {
			log.error("获取分类列表失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	public ArticleCategory findByName(String categoryName) {
		Example example = new Example(ArticleCategory.class);
		example.createCriteria().andCondition("lower(name) =", categoryName.toLowerCase());
		List<ArticleCategory> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public ArticleCategory findBySlug(String slug) {
		Example example = new Example(ArticleCategory.class);
		example.createCriteria().andEqualTo("slug", slug);
		List<ArticleCategory> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public ArticleCategory findById(Long categoryId) {
		return this.selectByKey(categoryId);
	}

	@Override
	@Transactional
	public void addCategory(ArticleCategory category) {
		Long parentId = category.getParentId();
		if (parentId == null)
			category.setParentId(0L);
		category.setCreateTime(new Date());
		if (StringUtils.isBlank(category.getSlug())) {
			String slug = PinyinUtil.getFullPinyin(category.getName());
			category.setSlug(slug);
		}
		this.save(category);
	}

	@Override
	@Transactional
	public void updateCategory(ArticleCategory category) {
		if (StringUtils.isBlank(category.getSlug())) {
			String slug = PinyinUtil.getFullPinyin(category.getName());
			category.setSlug(slug);
		}
		this.updateNotNull(category);
	}

	@Override
	@Transactional
	public void deleteCategorys(String categoryIds) {
		List<String> list = Arrays.asList(categoryIds.split(","));
		this.batchDelete(list, "id", ArticleCategory.class);
		this.categoryMapper.changeToTop(list);

	}

	@Override
	public Tree<ArticleCategory> getCategoryChildTree(ArticleCategory category) {
		Tree<ArticleCategory> root = new Tree<ArticleCategory>();
		List<Tree<ArticleCategory>> trees = new ArrayList<>();
		List<ArticleCategory> childs = this.findCategoryByPid(category.getId());
		childs.forEach(cat -> {
			Tree<ArticleCategory> tree = new Tree<ArticleCategory>();
			tree.setId(cat.getId().toString());
			tree.setParentId(cat.getParentId().toString());
			tree.setText(cat.getName());
			trees.add(tree);
		});
		root.setId(category.getId().toString());
		root.setText(category.getName());
		TreeUtils.buildByParent(trees, root);
		return root;
	}

	@Override
	public List<ArticleCategory> findCategoryByPid(Long parentId) {
		List<ArticleCategory> lists = new ArrayList<ArticleCategory>();
		Example example = new Example(ArticleCategory.class);
		example.createCriteria().andEqualTo("parentId", parentId);
		List<ArticleCategory> childs = this.selectByExample(example);
		for (ArticleCategory child : childs) {
			List<ArticleCategory> subChilds = findCategoryByPid(child.getId());
			lists.addAll(subChilds);
		}
		lists.addAll(childs);
		return lists;
	}

	/**
	 * 查询一级子分类
	 */
	@Override
	public List<ArticleCategory> getChildCategory(Long parentId) {
		List<ArticleCategory> lists = new ArrayList<ArticleCategory>();
		Example example = new Example(ArticleCategory.class);
		example.createCriteria().andEqualTo("parentId", parentId);
		List<ArticleCategory> childs = this.selectByExample(example);
		lists.addAll(childs);
		return lists;
	}

	@Override
	public ArticleCategory findByIdSlug(String idSlug) {
		if (StringUtils.isNumeric(idSlug)) {
			return this.categoryMapper.selectByPrimaryKey(idSlug);
		} else {
			return this.findBySlug(idSlug);
		}
	}
}

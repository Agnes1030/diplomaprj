package com.febs.shop.service.impl;

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

import com.febs.common.domain.Tree;
import com.febs.common.service.impl.BaseService;
import com.febs.common.utils.PinyinUtil;
import com.febs.common.utils.TreeUtils;
import com.febs.shop.dao.ProductCategoryMapper;
import com.febs.shop.domain.ProductCategory;
import com.febs.shop.service.ProductCategoryService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductCategoryServiceImpl extends BaseService<ProductCategory> implements ProductCategoryService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductCategoryMapper productCategoryMapper;
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;

	@Override
	public List<ProductCategory> getChildProductCategory(Long parentId) {
		List<ProductCategory> lists = new ArrayList<ProductCategory>();
		Example example = new Example(ProductCategory.class);
		example.createCriteria().andEqualTo("parentId", parentId);
		List<ProductCategory> childs = this.selectByExample(example);
		lists.addAll(childs);
		return lists;
	}

	@Override
	public List<ProductCategory> findProductCategoryByPid(Long parentId) {
		List<ProductCategory> lists = new ArrayList<ProductCategory>();
		Example example = new Example(ProductCategory.class);
		example.createCriteria().andEqualTo("parentId", parentId);
		List<ProductCategory> childs = this.selectByExample(example);
		for (ProductCategory child : childs) {
			List<ProductCategory> subChilds = findProductCategoryByPid(child.getId());
			lists.addAll(subChilds);
		}
		lists.addAll(childs);
		return lists;
	}

	@Override
	public Tree<ProductCategory> getProductCategoryChildTree(ProductCategory category) {
		List<Tree<ProductCategory>> trees = new ArrayList<>();
		List<ProductCategory> categorys = this.findAllProductCategorys(category);
		categorys.forEach(cat -> {
			Tree<ProductCategory> tree = new Tree<ProductCategory>();
			tree.setId(cat.getId().toString());
			tree.setParentId(cat.getParentId().toString());
			tree.setText(cat.getName());
			trees.add(tree);
		});
		return TreeUtils.build(trees);
	}

	@Override
	public Tree<ProductCategory> getProductCategoryTree(ProductCategory category) {
		List<Tree<ProductCategory>> trees = new ArrayList<>();
		List<ProductCategory> categorys = this.findAllProductCategorys(category);
		categorys.forEach(cat -> {
			Tree<ProductCategory> tree = new Tree<ProductCategory>();
			tree.setId(cat.getId().toString());
			tree.setParentId(cat.getParentId().toString());
			tree.setText(cat.getName());
			trees.add(tree);
		});
		return TreeUtils.build(trees);
	}

	@Override
	public List<ProductCategory> findAllProductCategorys(ProductCategory category) {
		try {
			Example example = new Example(ProductCategory.class);
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
	public ProductCategory findByName(String categoryName) {
		Example example = new Example(ProductCategory.class);
		example.createCriteria().andCondition("lower(name) =", categoryName.toLowerCase());
		List<ProductCategory> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public ProductCategory findBySlug(String slug) {
		Example example = new Example(ProductCategory.class);
		example.createCriteria().andEqualTo("slug", slug);
		List<ProductCategory> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public ProductCategory findById(Long categoryId) {
		return this.selectByKey(categoryId);
	}

	@Override
	public void addProductCategory(ProductCategory category) {
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
	public void updateProductCategory(ProductCategory category) {
		if (StringUtils.isBlank(category.getSlug())) {
			String slug = PinyinUtil.getFullPinyin(category.getName());
			category.setSlug(slug);
		}
		this.updateNotNull(category);

	}

	@Override
	public void deleteProductCategorys(String categoryIds) {
		List<String> list = Arrays.asList(categoryIds.split(","));
		this.batchDelete(list, "id", ProductCategory.class);
		this.productCategoryMapper.changeToTop(list);

	}

	@Override
	public ProductCategory findByIdSlug(String idSlug) {
		if (StringUtils.isNumeric(idSlug)) {
			return this.productCategoryMapper.selectByPrimaryKey(idSlug);
		} else {
			return this.findBySlug(idSlug);
		}
	}

}

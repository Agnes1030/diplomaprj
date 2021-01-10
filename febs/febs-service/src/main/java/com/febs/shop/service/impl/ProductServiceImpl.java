package com.febs.shop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.ProductMapper;
import com.febs.shop.domain.Product;
import com.febs.shop.service.ProductService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductServiceImpl extends BaseService<Product> implements ProductService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductMapper productMapper;

	@Override
	public Product findByIdSlug(String idSlug) {
		if (StringUtils.isNumeric(idSlug)) {
			return this.productMapper.selectByPrimaryKey(idSlug);
		} else {
			return this.findBySlug(idSlug);
		}

	}

	@Override
	public Product findById(Long productId) {
		return this.selectByKey(productId);
	}

	@Override
	public void updateProduct(Product product) {
		product.setModifiedTime(new Date());
		this.updateNotNull(product);

	}

	@Override
	public void addProduct(Product product) {
		product.setCreateTime(new Date());
		product.setModifiedTime(new Date());
		this.save(product);
	}

	@Override
	public void deleteProducts(String productIds) {
		List<String> list = Arrays.asList(productIds.split(","));
		this.batchDelete(list, "id", Product.class);

	}

	@Override
	public void updateViewCount(Long id) {
		productMapper.updateViewCount(id);
	}

	@Override
	public List<Product> findAllProducts(Product product, List<Long> ids) {
		try {
			return this.productMapper.queryProducts(product, ids);
		} catch (Exception e) {
			log.error("获取文章列表失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	public List<Product> findHotProducts(Product product, List<Long> ids) {
		try {
			return this.productMapper.queryHotProducts(product, ids);
		} catch (Exception e) {
			log.error("获取产品列表失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	public Product findBySlug(String slug) {
		Example example = new Example(Product.class);
		example.createCriteria().andEqualTo("slug", slug);
		List<Product> list = this.selectByExample(example);
		return list.size() == 0 ? null : list.get(0);
	}

	@Override
	public Integer updateAddSaleCount(Long productId, Integer addCount) {
		return this.productMapper.updateAddSaleCount(productId, addCount);
	}

}

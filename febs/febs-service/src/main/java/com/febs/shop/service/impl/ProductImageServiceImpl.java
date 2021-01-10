package com.febs.shop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.domain.ProductImage;
import com.febs.shop.service.ProductImageService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductImageServiceImpl extends BaseService<ProductImage> implements ProductImageService {

	@Override
	public List<ProductImage> getProductImgs(Long productId) {
		Example example = new Example(ProductImage.class);
		example.createCriteria().andEqualTo("productId", productId);
		List<ProductImage> list = this.selectByExample(example);
		return list;
	}

	@Override
	public int deleteProductImgs(Long productId) {
		Example example = new Example(ProductImage.class);
		example.createCriteria().andEqualTo("productId", productId);
		return this.getMapper().deleteByExample(example);
	}

}

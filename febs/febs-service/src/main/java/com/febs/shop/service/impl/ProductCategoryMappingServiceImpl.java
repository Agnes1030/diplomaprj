package com.febs.shop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.domain.ProductCategoryMapping;
import com.febs.shop.service.ProductCategoryMappingService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductCategoryMappingServiceImpl extends BaseService<ProductCategoryMapping>
		implements ProductCategoryMappingService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Integer deleteProductCategoryMapping(Long productId) {
		log.debug("删除标签商品关系productId=" + productId);
		// 删除商品的所有关联关系
		Example example = new Example(ProductCategoryMapping.class);
		example.createCriteria().andEqualTo("productId", productId);

		return this.getMapper().deleteByExample(example);
	}

	@Override
	public Integer batchDeleteCategoryMapping(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		return this.batchDelete(list, "productId", ProductCategoryMapping.class);
	}

}

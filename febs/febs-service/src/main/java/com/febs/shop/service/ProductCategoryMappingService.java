package com.febs.shop.service;

import com.febs.shop.domain.ProductCategoryMapping;
import com.febs.common.service.IService;

public interface ProductCategoryMappingService extends IService<ProductCategoryMapping> {
	public Integer deleteProductCategoryMapping(Long productId);
	public Integer batchDeleteCategoryMapping(String ids);
}

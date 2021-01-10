package com.febs.shop.service;

import com.febs.common.service.IService;
import com.febs.shop.domain.ProductSku;

public interface ProductSkuService extends IService<ProductSku> {
	public int deleteByCondition(Object example);
	public Integer updateStock(Long skuId,Integer num);
}

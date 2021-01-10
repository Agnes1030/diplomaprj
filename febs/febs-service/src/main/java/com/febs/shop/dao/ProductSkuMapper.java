package com.febs.shop.dao;

import org.apache.ibatis.annotations.UpdateProvider;

import com.febs.common.config.MyMapper;
import com.febs.shop.domain.ProductSku;

public interface ProductSkuMapper extends MyMapper<ProductSku> {
	public static class skuProvider{
		public static String updateStock(Long skuId,Integer num) {
			return "update t_product_sku set stock=#{num} where id=#{skuId}";
		}
	}
	@UpdateProvider(type = skuProvider.class, method = "updateStock")
	public Integer updateStock(Long skuId,Integer num);

}

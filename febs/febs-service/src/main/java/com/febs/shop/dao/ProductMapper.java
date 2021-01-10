package com.febs.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import com.febs.common.config.MyMapper;
import com.febs.shop.domain.Product;

public interface ProductMapper extends MyMapper<Product> {
	public static class ProductProvider {
		public static String updateAddSaleCount(Long productId, Integer addCount) {
			return "update  t_product set sales_count=sales_count+#{addCount} where id=#{productId}";
		}
	}

	@UpdateProvider(type = ProductProvider.class, method = "updateAddSaleCount")
	public Integer updateAddSaleCount(Long productId, Integer addCount);

	public List<Product> queryProducts(@Param("product") Product product, @Param("ids") List<Long> ids);

	public List<Product> queryHotProducts(@Param("product") Product product, @Param("ids") List<Long> ids);

	public void updateViewCount(Long id);
}

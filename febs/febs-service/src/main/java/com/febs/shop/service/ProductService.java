package com.febs.shop.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.shop.domain.Product;

public interface ProductService extends IService<Product> {
	public Product findByIdSlug(String idSlug);

	public Product findBySlug(String slug);

	public Product findById(Long productId);

	public void updateProduct(Product product);

	public void addProduct(Product product);

	public void deleteProducts(String productIds);

	public void updateViewCount(Long id);

	public List<Product> findAllProducts(Product product, List<Long> ids);

	public List<Product> findHotProducts(Product product, List<Long> ids);
	public Integer updateAddSaleCount(Long productId, Integer addCount);
}

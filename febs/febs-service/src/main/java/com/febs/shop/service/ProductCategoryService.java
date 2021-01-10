package com.febs.shop.service;

import java.util.List;

import com.febs.common.domain.Tree;
import com.febs.common.service.IService;
import com.febs.shop.domain.ProductCategory;

public interface ProductCategoryService extends IService<ProductCategory> {
	List<ProductCategory> getChildProductCategory(Long parentId);

	List<ProductCategory> findProductCategoryByPid(Long parentId);

	Tree<ProductCategory> getProductCategoryChildTree(ProductCategory category);

	Tree<ProductCategory> getProductCategoryTree(ProductCategory category);

	List<ProductCategory> findAllProductCategorys(ProductCategory category);

	ProductCategory findByName(String categoryName);

	ProductCategory findBySlug(String slug);
	ProductCategory findByIdSlug(String idSlug);
	ProductCategory findById(Long categoryId);

	void addProductCategory(ProductCategory category);

	void updateProductCategory(ProductCategory category);

	void deleteProductCategorys(String categoryIds);

}

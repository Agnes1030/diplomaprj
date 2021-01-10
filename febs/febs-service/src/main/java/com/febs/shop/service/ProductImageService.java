package com.febs.shop.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.shop.domain.ProductImage;

public interface ProductImageService extends IService<ProductImage> {
	public List<ProductImage> getProductImgs(Long productId);
	public int deleteProductImgs(Long productId);

}

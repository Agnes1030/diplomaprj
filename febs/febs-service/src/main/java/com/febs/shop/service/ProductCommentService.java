package com.febs.shop.service;

import com.febs.common.service.IService;
import com.febs.shop.domain.ProductComment;

public interface ProductCommentService extends IService<ProductComment> {
	public Integer deleteProductComments(String ids);

}

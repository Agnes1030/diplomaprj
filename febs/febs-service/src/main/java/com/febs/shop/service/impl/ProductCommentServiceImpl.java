package com.febs.shop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.domain.ProductComment;
import com.febs.shop.service.ProductCommentService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProductCommentServiceImpl extends BaseService<ProductComment> implements ProductCommentService {

	@Override
	public Integer deleteProductComments(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		return this.batchDelete(list, "id", ProductComment.class);
	}

}

package com.febs.web.controller.shop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.common.domain.QueryRequest;
import com.febs.shop.domain.ProductComment;
import com.febs.shop.service.ProductCommentService;
import com.febs.web.controller.base.WebBaseController;

import tk.mybatis.mapper.entity.Example;

@Controller
public class ShopProductController extends WebBaseController {
	@Autowired
	private ProductCommentService productCommentService;

	@RequestMapping("/productComment/list")
	@ResponseBody
	public Map<String, Object> productCommentList(QueryRequest request, Long productId) {
		Example commentExample = new Example(ProductComment.class);
		commentExample.selectProperties("author","title","content");
		commentExample.createCriteria().andEqualTo("productId", productId).andEqualTo("status",ProductComment.STATUS_VALID);
		return super.selectByPageNumSize(request, () -> productCommentService.selectByExample(commentExample));
	}
}

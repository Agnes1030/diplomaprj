package com.febs.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.febs.FebsApplication;
import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.common.domain.Tree;

@SpringBootTest(classes = FebsApplication.class)
public class CategoryServiceTest {
	@Autowired
	ArticleCategoryService categoryService;

	@Test
	public void categoryTreeTest() {
		ArticleCategory category = categoryService.findById(1L);
		Tree<ArticleCategory> tree = categoryService.getCategoryChildTree(category);
		System.out.println("子节点:" + tree.getChildren().size());

	}
}

package com.febs.test;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.febs.FebsApplication;
import com.febs.cms.dao.ArticleMapper;
import com.febs.cms.domain.Article;
import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.service.ArticleService;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.Tree;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@SpringBootTest(classes = FebsApplication.class)
public class ArticleServiceTest {
	@Autowired
	ArticleCategoryService categoryService;
	@Autowired
	ArticleService articleService;
	@Autowired
	ArticleMapper articleMapper;
	@Test
	public void testSelect() {
		String content = articleMapper.getArticleContent(1L);
		System.out.println("内容:"+content);
	}
	protected PageInfo<?>  selectByPageNumSize(QueryRequest request, Supplier<?> s) {
		PageHelper.startPage(request.getPageNum(), request.getPageSize());
		PageInfo<?> pageInfo = new PageInfo<>((List<?>) s.get());
		PageHelper.clearPage();
		return pageInfo;
	}
	//@Test
	public void categoryTreeTest() {
		Article article =new Article();
	List<Article> articles= articleService.findAllArticles(article, null);
	System.out.println("size:"+articles.size());
	QueryRequest request=new QueryRequest();
	request.setPageNum(2);
	request.setPageSize(2);
	PageInfo<?> hotArticles= selectByPageNumSize(request,()->articleService.findHotArticles(4L)) ;
	System.out.println("hotArticles:"+hotArticles.getPageSize());
	}
}

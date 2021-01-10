package com.febs.test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.febs.FebsApplication;
import com.febs.common.domain.QueryRequest;
import com.febs.search.lucene.model.PageQuery;
import com.febs.search.lucene.service.LuceneService;
import com.febs.shop.domain.Product;
import com.febs.shop.service.ProductService;
import com.febs.shop.service.ProductTopicService;
import com.github.pagehelper.PageInfo;

import net.minidev.json.JSONUtil;

//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
// 这里很重要，必须要是应用程序的主类 FebsApplication.class ，注意不要写成了当前测试类ArticleControllerTest.cass
@SpringBootTest(classes = FebsApplication.class)
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
public class ProductSearchTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private ProductTopicService productTopicService;
	private MockMvc mockMvc;
	@Autowired
	private ProductService productService;
	@Autowired
	private LuceneService luceneService;

	@BeforeEach
	public void setUp() throws Exception {
		// MockMvcBuilders.webAppContextSetup(WebApplicationContext
		// context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();// 建议使用这种
	}

	@Test
	public void testCreateProducts() {

		List<Product> products = productTopicService.getTopicProducts(2L);
		try {
			luceneService.createProductIndex(products);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("查找专题商品数量:" + products.size());
	}

	@Test
	public void testFind() throws Exception {
		QueryRequest queryRequest=new QueryRequest();
		Product pro = new Product();
		pro.setKeywords("土豆");
		PageInfo pageInfo = luceneService.searchProduct(queryRequest,pro);
		System.out.println("检索结果:"+JSON.toJSONString(pageInfo.getList()));
	}
}

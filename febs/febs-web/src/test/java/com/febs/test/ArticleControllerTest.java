package com.febs.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.febs.FebsApplication;
import com.febs.apisecurity.service.TokenService;
import com.febs.cms.domain.Article;
import com.febs.cms.domain.ArticleCategoryMapping;
import com.febs.cms.service.ArticleCategoryMappingService;
import com.febs.cms.service.ArticleService;
import com.febs.cms.service.ArticleTagMappingService;
import com.febs.cms.service.ArticleTagService;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.system.domain.MyUser;

//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
// 这里很重要，必须要是应用程序的主类 FebsApplication.class ，注意不要写成了当前测试类ArticleControllerTest.cass
@SpringBootTest(classes = FebsApplication.class)
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
public class ArticleControllerTest {
	@Autowired
    private WebApplicationContext webApplicationContext;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleCategoryService categoryService;
	@Autowired
	private ArticleTagService articleTagService;
	@Autowired
	private ArticleTagMappingService articleTagMappingService;
	@Autowired
	private ArticleCategoryMappingService articleCategoryService;
	@Autowired
	private ArticleCategoryMappingService articleCategoryMappingService;
	@Autowired
	private TokenService tokenService;
    private MockMvc mockMvc;
	@Autowired
	private PasswordEncoder passwordEncoder;
    @BeforeEach
    public void setUp() throws Exception{
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }
    @Test
    public void testToken() {
    	MyUser user=new MyUser();
    	user.setUserId(1L);
    	user.setUsername("admin");
    	user.setPassword("$2a$10$WRUqPfZUAhRgwaiuBPE0XeUoPZKFgGTkRjaU04dHnL99leE4kBv1W");
    	for(int i=0;i<4;i++) {
    		String jwt = tokenService.getToken(user);
    		System.out.println(i+"="+jwt);
    	}
    	
    }
  //  @Test
    public void testArticleMapping() {
    	Article article=new Article();
    	List<Long> ids=new ArrayList<Long>();
    	List<Article> articles=articleService.findAllArticles(article, ids);
    	for(Article art:articles) {
    		String[] catids = art.getCategoryIds().split(",");
    		articleCategoryMappingService.deleteArticleCategoryMapping(art.getId());
    		for(String cid:catids) {
    			ArticleCategoryMapping artMapping=new ArticleCategoryMapping();
    			Long lcid = Long.parseLong(cid);
    			artMapping.setArticleId(art.getId());
    			artMapping.setCategoryId(lcid);
    			articleCategoryMappingService.save(artMapping);
    			
    		}
    	}
    }
    //@Test
    public void testAddComment() {
    	 try {
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/cms/articleComment/add")
			         .accept(MediaType.APPLICATION_JSON).param("content", "我222mock的评论内容").param("articleId","2"))
			         .andExpect(MockMvcResultMatchers.status().isOk())
			         .andDo(MockMvcResultHandlers.print())
			         .andReturn();
			System.out.print(mvcResult.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

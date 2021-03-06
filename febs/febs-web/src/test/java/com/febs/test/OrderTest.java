package com.febs.test;

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

import com.febs.FebsApplication;
import com.febs.shop.dao.OrderMapper;
import com.febs.shop.dao.ProductSkuMapper;

//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
// 这里很重要，必须要是应用程序的主类 FebsApplication.class ，注意不要写成了当前测试类ArticleControllerTest.cass
@SpringBootTest(classes = FebsApplication.class)
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
public class OrderTest {
	@Autowired
    private WebApplicationContext webApplicationContext;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private ProductSkuMapper skuMapper;
    private MockMvc mockMvc;
	@Autowired
	private PasswordEncoder passwordEncoder;
    @BeforeEach
    public void setUp() throws Exception{
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }
    @Test
    public void testUpdateStock() {
    	skuMapper.updateStock(284L, 36);
    }
  //  @Test
    public void testUpdateRemark() {

    	orderMapper.updateRemarks("我的测试remarks", "1");
    }

}

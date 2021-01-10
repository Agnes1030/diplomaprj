package com.febs.security.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import com.febs.common.domain.FebsConstant;
import com.febs.security.code.ValidateCodeGenerator;
import com.febs.security.code.img.ImageCodeFilter;
import com.febs.security.code.img.ImageCodeGenerator;
import com.febs.security.code.sms.DefaultSmsSender;
import com.febs.security.code.sms.SmsCodeFilter;
import com.febs.security.code.sms.SmsCodeSender;
import com.febs.security.handler.FebsAuthenticationAccessDeniedHandler;
import com.febs.security.handler.FebsAuthenticationFailureHandler;
import com.febs.security.handler.FebsAuthenticationSucessHandler;
import com.febs.security.handler.FebsLogoutHandler;
import com.febs.security.properties.FebsSecurityProperties;
import com.febs.security.service.FebsUserDetailService;
import com.febs.security.session.FebsExpiredSessionStrategy;
import com.febs.security.session.FebsInvalidSessionStrategy;
import com.febs.security.xss.XssFilter;

/**
 * security 配置中心
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FebsSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private FebsAuthenticationSucessHandler febsAuthenticationSucessHandler;

	@Autowired
	private FebsAuthenticationFailureHandler febsAuthenticationFailureHandler;

	@Autowired
	private FebsSecurityProperties febsSecurityProperties;

	@Autowired
	private FebsSmsCodeAuthenticationSecurityConfig febsSmsCodeAuthenticationSecurityConfig;

	@Autowired
	private FebsUserDetailService febsUserDetailService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SpringSocialConfigurer febsSocialSecurityConfig;

	// spring security自带的密码加密工具类
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 处理 rememberMe 自动登录认证
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		jdbcTokenRepository.setCreateTableOnStartup(false);
		return jdbcTokenRepository;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		String[] anonResourcesUrl = StringUtils
				.splitByWholeSeparatorPreserveAllTokens(febsSecurityProperties.getAnonResourcesUrl(), ",");

		ImageCodeFilter imageCodeFilter = new ImageCodeFilter();
		imageCodeFilter.setAuthenticationFailureHandler(febsAuthenticationFailureHandler);
		imageCodeFilter.setSecurityProperties(febsSecurityProperties);
		imageCodeFilter.afterPropertiesSet();

		SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
		smsCodeFilter.setAuthenticationFailureHandler(febsAuthenticationFailureHandler);
		smsCodeFilter.setSecurityProperties(febsSecurityProperties);
		smsCodeFilter.setSessionRegistry(sessionRegistry());
		smsCodeFilter.afterPropertiesSet();
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()) // 权限不足处理器
				.and().addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class) // 短信验证码校验
				.addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加图形证码校验过滤器
				.formLogin() // 表单方式
				.loginPage(febsSecurityProperties.getMemberLogin()) // 未认证跳转 URL
				.loginProcessingUrl(febsSecurityProperties.getCode().getImage().getLoginProcessingUrl()) // 处理登录认证 URL
				.successHandler(febsAuthenticationSucessHandler) // 处理登录成功
				.failureHandler(febsAuthenticationFailureHandler) // 处理登录失败
				.and().headers().frameOptions().sameOrigin()  // 允许同源iframe嵌套
				.and().rememberMe() // 添加记住我功能
				.tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
				.tokenValiditySeconds(febsSecurityProperties.getRememberMeTimeout()) // rememberMe 过期时间，单为秒
				.userDetailsService(febsUserDetailService) // 处理自动登录逻辑
				.and().sessionManagement() // 配置 session管理器
				.invalidSessionUrl(febsSecurityProperties.getInvalidSessionUrl())
			    .invalidSessionStrategy(invalidSessionStrategy()) // 处理 session失效
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.maximumSessions(febsSecurityProperties.getSession().getMaximumSessions()) // 最大并发登录数量
				.expiredSessionStrategy(new FebsExpiredSessionStrategy()) // 处理并发登录被踢出
				.sessionRegistry(sessionRegistry()) // 配置 session注册中心
				.and().and().logout() // 配置登出
				.addLogoutHandler(logoutHandler()) // 配置登出处理器
				.logoutUrl(febsSecurityProperties.getLogoutUrl()) // 处理登出 url
				.logoutSuccessUrl(febsSecurityProperties.getMemberLogin()) // 登出后跳转到 /
				.deleteCookies("JSESSIONID","SESSION") // 删除 JSESSIONID和SESSION 否则会出现 重定向次数过多的问题，因为本地原来SESSION似乎还保留
				.and().authorizeRequests() // 授权配置
				.antMatchers(anonResourcesUrl).permitAll() // 免认证静态资源路径
				.antMatchers(febsSecurityProperties.getAdminLogin(), // 登录路径
						febsSecurityProperties.getMemberLogin(),
						FebsConstant.FEBS_WEB_TOREGISTH,
						FebsConstant.FEBS_WEB_REGIST,
						FebsConstant.FEBS_REGIST_URL, // 用户注册 url
						"/swagger-ui.html",
						febsSecurityProperties.getCode().getImage().getCreateUrl(), // 创建图片验证码路径
						febsSecurityProperties.getCode().getSms().getCreateUrl(), // 创建短信验证码路径
						febsSecurityProperties.getSocial().getSocialRedirectUrl(), // 重定向到社交账号注册（绑定）页面路径
						febsSecurityProperties.getSocial().getSocialBindUrl(), // 社交账号绑定 URL
						febsSecurityProperties.getSocial().getSocialRegistUrl() // 注册并绑定社交账号 URL
				).permitAll() // 配置免认证路径
				.antMatchers(febsSecurityProperties.getAdminUrl()).hasAnyRole(febsSecurityProperties.getAdminRoles())// 超级管理员和普通管理员才可以登录后台
				.antMatchers(febsSecurityProperties.getMemberUrl()).authenticated()// 普通用户个人中心页，只要密码验证通过就可以访问
				.anyRequest().permitAll()
				.and().csrf().disable().apply(febsSmsCodeAuthenticationSecurityConfig) // 添加短信验证码认证流程
				.and().apply(febsSocialSecurityConfig); // social 配置
	}

	@Bean
	@ConditionalOnMissingBean(name = "imageCodeGenerator")
	public ValidateCodeGenerator imageCodeGenerator() {
		ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
		imageCodeGenerator.setSecurityProperties(febsSecurityProperties);
		return imageCodeGenerator;
	}

	@Bean
	@ConditionalOnMissingBean(SmsCodeSender.class)
	public SmsCodeSender smsCodeSender() {
		return new DefaultSmsSender();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	// 使用 javaconfig 的方式配置是为了注入 sessionRegistry
	@Bean
	public FebsAuthenticationSucessHandler febsAuthenticationSucessHandler() {
		FebsAuthenticationSucessHandler authenticationSucessHandler = new FebsAuthenticationSucessHandler();
		authenticationSucessHandler.setSessionRegistry(sessionRegistry());
		return authenticationSucessHandler;
	}

	// 配置登出处理器
	@Bean
	public LogoutHandler logoutHandler() {
		FebsLogoutHandler febsLogoutHandler = new FebsLogoutHandler();
		febsLogoutHandler.setSessionRegistry(sessionRegistry());
		return febsLogoutHandler;
	}

	@Bean
	public InvalidSessionStrategy invalidSessionStrategy() {
		FebsInvalidSessionStrategy febsInvalidSessionStrategy = new FebsInvalidSessionStrategy();
		febsInvalidSessionStrategy.setSecurityProperties(febsSecurityProperties);
		return febsInvalidSessionStrategy;
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new FebsAuthenticationAccessDeniedHandler();
	}

	/**
	 * XssFilter Bean
	 */
	@Bean
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterRegistrationBean xssFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new XssFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
		initParameters.put("isIncludeRichText", "true");
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}

}

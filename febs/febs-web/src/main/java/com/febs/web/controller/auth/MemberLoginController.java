package com.febs.web.controller.auth;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.security.properties.FebsSecurityProperties;

@Controller
@RequestMapping("/member")
public class MemberLoginController {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Autowired
	private FebsSecurityProperties febsSecurityProperties;
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;

	// 匹配 /member
	@GetMapping("")
	public void success(HttpServletRequest request, HttpServletResponse response) throws IOException {
		redirectStrategy.sendRedirect(request, response, "/member/profile");
	}

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String redirectUrl = savedRequest.getRedirectUrl();
			log.info("引发跳转的请求是：{}", redirectUrl);
		}
		return "member/login";
	}

	@GetMapping("/index")
	public String adminIndex(HttpServletRequest request, HttpServletResponse response) {
	    return "member/index";
	}
}

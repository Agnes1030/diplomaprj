package com.febs.web.controller.auth;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.febs.common.domain.QueryRequest;
import com.febs.common.utils.DateUtil;
import com.febs.security.properties.FebsSecurityProperties;
import com.febs.shop.domain.Order;
import com.febs.shop.service.OrderService;
import com.febs.system.service.MenuService;
import com.febs.system.service.UserService;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class AdminLoginController extends BaseController {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Autowired
	private FebsSecurityProperties febsSecurityProperties;
	@Autowired
	private MenuService menuService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;

	// 匹配 /admin
	@GetMapping("")
	public void success(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 登录成功以后要加载用户的菜单相关信息
		redirectStrategy.sendRedirect(request, response, febsSecurityProperties.getAdminIndex());
	}

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String redirectUrl = savedRequest.getRedirectUrl();
			log.info("引发跳转的请求是：{}", redirectUrl);
		}
		return "admin/login";
	}

	@GetMapping("/index")
	public String adminIndex(Authentication authentication, Model model) {
		return "admin/index";
	}
	@GetMapping("/main")
	public String adminMain(Authentication authentication, Model model) {
		QueryRequest request=new QueryRequest();
		request.setPageNum(1);
		request.setPageSize(5);
		List<Order> orders = (List<Order>)this.selectByPageNumSize(request, ()->orderService.selectAll()).get("rows");
		String dateStr = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd")+" 00:00:00";
		int todayOrderCount = orderService.queryDateAfterCount(dateStr);
		int todayPayCount = orderService.queryPayAfterCount(dateStr);
		int todayUserCount = userService.queryDateAfterCount(dateStr);
		model.addAttribute("user", authentication.getPrincipal());
		model.addAttribute("orders",orders);
		model.addAttribute("orderCount",todayOrderCount);
		model.addAttribute("userCount",todayUserCount);
		model.addAttribute("payorderCount",todayPayCount);
		return "admin/admin_main";
	}
}

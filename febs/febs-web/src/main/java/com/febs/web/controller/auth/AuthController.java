package com.febs.web.controller.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.febs.common.domain.FebsConstant;
import com.febs.common.domain.ResponseBo;
import com.febs.security.properties.FebsSecurityProperties;
import com.febs.security.xss.JsoupUtil;
import com.febs.system.domain.MyUser;
import com.febs.system.service.UserService;
import com.febs.web.controller.base.BaseController;

@Controller
public class AuthController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private FebsSecurityProperties febsSecurityProperties;

	@GetMapping(value=FebsConstant.FEBS_WEB_TOREGISTH)
	public String socialIndex(HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {
		return "regist";
	}

	@PostMapping(value=FebsConstant.FEBS_WEB_REGIST)
	@ResponseBody
	public ResponseBo socialRegist(HttpServletRequest request, MyUser user) {
		try {
			if(null==user ||StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())) {
				throw new Exception("注册失败，请输入完整用户信息");
			}else {
				String username = user.getUsername();
				user.setUsername(JsoupUtil.clean(username));
			}
			MyUser result = this.userService.findByName(user.getUsername());
			if (result != null) {
				return ResponseBo.warn("该用户名已被使用！");
			}
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			user.setStatus(MyUser.STATUS_VALID);
			// 11为外部部门ID号，固定值
			user.setDeptId(11L);
			this.userService.registUser(user);
			return ResponseBo.ok();
		} catch (Exception e) {
			log.error("注册失败", e);
			return ResponseBo.error("注册失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/login/invalid")
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public String invalid() {
		return "Session 已过期，请重新登录";
	}
}

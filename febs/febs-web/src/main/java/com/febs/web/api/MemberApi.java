package com.febs.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.febs.apisecurity.annotation.UserLoginToken;
import com.febs.apisecurity.service.TokenService;
import com.febs.common.service.RedisService;
import com.febs.system.domain.MyUser;
import com.febs.system.service.UserService;

/**
 * api权限校验
 */
@RestController
@RequestMapping("webapi")
public class MemberApi {
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RedisService redisService;
	@Value("${febs.jwt.token_expire}")
	private Long expireTime;

	// 登录
	@PostMapping("/member/login")
	public Object login(MyUser user) {
		JSONObject jsonObject = new JSONObject();
		MyUser userForBase = userService.findByName(user.getUsername());
		if (userForBase == null) {
			jsonObject.put("message", "登录失败,用户不存在");
			return jsonObject;
		} else {
			if (!passwordEncoder.matches(user.getPassword(), userForBase.getPassword())) {
				jsonObject.put("message", "登录失败,密码错误");
				return jsonObject;
			} else {
				String token = tokenService.getToken(userForBase);
				String key="token_"+userForBase.getUserId();
				// 15分钟有效期
				redisService.set(key, token,expireTime);
				jsonObject.put("token", token);
				jsonObject.put("user", userForBase);
				return jsonObject;
			}
		}
	}

	@UserLoginToken
	@GetMapping("/member/getMessage")
	public String getMessage() {
		return "你已通过验证";
	}
}

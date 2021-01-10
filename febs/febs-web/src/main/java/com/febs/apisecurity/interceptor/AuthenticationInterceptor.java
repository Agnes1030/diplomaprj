package com.febs.apisecurity.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.febs.apisecurity.annotation.PassToken;
import com.febs.apisecurity.annotation.UserLoginToken;
import com.febs.common.service.RedisService;
import com.febs.system.domain.MyUser;
import com.febs.system.service.UserService;

/**
 * @author mhm
 *
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	@Value("${febs.jwt.token_expire}")
	private Long expireTime;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object object) throws Exception {
		String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
		// 如果不是映射到方法直接通过
		if (!(object instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) object;
		Method method = handlerMethod.getMethod();
		// 检查是否有passtoken注释，有则跳过认证
		if (method.isAnnotationPresent(PassToken.class)) {
			PassToken passToken = method.getAnnotation(PassToken.class);
			if (passToken.required()) {
				return true;
			}
		}
		// 检查有没有需要用户权限的注解
		if (method.isAnnotationPresent(UserLoginToken.class)) {
			UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
			if (userLoginToken.required()) {
				// 执行认证
				if (token == null) {
					throw new RuntimeException("无token，请重新登录");
				}
				// 获取 token 中的 user id
				String userId;
				try {
					userId = JWT.decode(token).getAudience().get(0);
				} catch (JWTDecodeException j) {
					throw new RuntimeException("401");
				}
				MyUser user = userService.selectByKey(userId);
				if (user == null) {
					throw new RuntimeException("用户不存在，请重新登录");
				}
				// 验证 token
				JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
				try {
					jwtVerifier.verify(token);
					// 验证通过以后再判断是否已经失效
					String key = "token_" + userId;
					if (null == redisService.get(key)) {
						// 已经失效
						throw new RuntimeException("401");
					} else {
						// 重新生成token并刷新
						// 如果验证成功并且没有失效，就需要刷新过期时间
						redisService.pexpire(key, expireTime);
					}
				} catch (JWTVerificationException e) {
					throw new RuntimeException("401");
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {

	}
}

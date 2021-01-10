package com.febs.web.common.thymeleaf.cms.directive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.security.domain.FebsSocialUserDetails;
import com.febs.security.domain.FebsUserDetails;
import com.febs.shop.domain.ShoppingCart;
import com.febs.shop.service.ShoppingCartService;
import com.febs.system.domain.MyUser;
/**
 * 购物车输出标签
 * @author 自由的路 
 * @date 2020-06-11
 */
public class ShopCartProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "cartList";// 标签名
	public static final int PRECEDENCE = 200;

	public ShopCartProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

// pageSize 和pageNum 都是可选参数
//    <cms:cartList>
//    <div th:each="cartItem:${cartList}" >
//      <span th:text="${cartItem.name}">Julius Caesar</span>
//    </div>
//   </cms:cartList>
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final IElementTagStructureHandler structureHandler) {
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		WebEngineContext ctx = (WebEngineContext) context;
		HttpServletRequest req = ctx.getRequest();
		HttpServletResponse resp = ctx.getResponse();
		ShoppingCartService ShoppingCartService = appCtx.getBean(ShoppingCartService.class);
		MyUser myUser = getCurrentUser();
		String key = ShoppingCartService.getKey(req, resp, myUser);
		ShoppingCart cacheCart = ShoppingCartService.mergeCart(key, myUser);
		structureHandler.setLocalVariable("cartItems", cacheCart.getCartItems());
		// 只清除标签本身，但内容还存在
		structureHandler.removeTags();
		// 整个元素都删除，内容也不存在
		// structureHandler.removeElement();
	}

	protected MyUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		MyUser user = new MyUser();
		if (principal instanceof FebsUserDetails) {
			FebsUserDetails userDetails = (FebsUserDetails) principal;
			user.setUserId(userDetails.getUserId());
			user.setPassword(userDetails.getPassword());
			user.setUsername(userDetails.getUsername());
		} else {
			FebsSocialUserDetails userDetails = (FebsSocialUserDetails) principal;
			user.setUserId(userDetails.getUsersId());
			user.setPassword(userDetails.getPassword());
			user.setUsername(userDetails.getUsername());
		}
		return user;
	}
}

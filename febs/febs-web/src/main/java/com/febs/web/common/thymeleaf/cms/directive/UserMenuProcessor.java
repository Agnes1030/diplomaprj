package com.febs.web.common.thymeleaf.cms.directive;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.common.domain.Tree;
import com.febs.security.domain.FebsSocialUserDetails;
import com.febs.security.domain.FebsUserDetails;
import com.febs.system.domain.Menu;
import com.febs.system.domain.MyUser;
import com.febs.system.service.MenuService;

/**
 * 后台用户菜单列表标签
 * 
 * @author 自由的路
 * @date 2020-06-11
 */
public class UserMenuProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "userMenus";// 标签名
	public static final int PRECEDENCE = 200;

	public UserMenuProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		// 当前登录用户
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		MenuService menuService = appCtx.getBean(MenuService.class);
		MyUser user = new MyUser();
		Object principal = authentication.getPrincipal();
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
		Tree<Menu> tree = menuService.getUserMenu(user.getUsername());
		structureHandler.setLocalVariable("menus", tree.getChildren());
		// 只清除标签本身，但内容还存在
		structureHandler.removeTags();
		// 整个元素都删除，内容也不存在
		// structureHandler.removeElement();
	}

}

package com.febs.web.common.thymeleaf.cms.directive;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.febs.system.domain.MyUser;
import com.febs.system.domain.RoleMenu;
import com.febs.system.domain.UserWithRole;
import com.febs.system.service.RoleMenuServie;
import com.febs.system.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class HasPermissonProcessor extends AbstractStandardConditionalVisibilityTagProcessor {
	public static final int PRECEDENCE = 300;
	public static final String ATTR_NAME = "haspermission";

	public HasPermissonProcessor(final TemplateMode templateMode, final String dialectPrefix) {
		super(templateMode, dialectPrefix, ATTR_NAME, PRECEDENCE);
	}

	@Override
	protected boolean isVisible(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue) {
		final IStandardExpressionParser expressionParser = StandardExpressions
				.getExpressionParser(context.getConfiguration());

		final IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
		final String menuId = (String) expression.execute(context);
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		WebEngineContext ctx = (WebEngineContext) context;
		UserService userService = appCtx.getBean(UserService.class);
		RoleMenuServie roleMenuService = appCtx.getBean(RoleMenuServie.class);
		ObjectMapper objectMapper = appCtx.getBean(ObjectMapper.class);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String principalJson;
		try {
			principalJson = objectMapper.writeValueAsString(principal);
			JsonNode node = objectMapper.readTree(principalJson);
			String userName = node.get("username").asText();
			MyUser user = userService.findByName(userName);
			UserWithRole userWithRole = userService.findById(user.getUserId());
			Example example = new Example(RoleMenu.class);
			Criteria critera = example.createCriteria();
			critera.andEqualTo("menuId", menuId);
			List<Long> roleIds = userWithRole.getRoleIds();
			critera.andIn("roleId", roleIds);
			List<RoleMenu> lists = roleMenuService.selectByExample(example);
			if (null != lists && lists.size() > 0) {
				return true;
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

}

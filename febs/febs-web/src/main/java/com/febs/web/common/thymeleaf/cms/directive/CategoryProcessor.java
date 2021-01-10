package com.febs.web.common.thymeleaf.cms.directive;

import java.math.BigInteger;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.service.ArticleCategoryService;
/**
 * 文章分类列表标签
 * @author 自由的路 
 * @date 2020-06-11
 */
public class CategoryProcessor extends AbstractElementTagProcessor{
	private static final String TAG_NAME = "categoryList";// 标签名
	private static final String PID="pid";
	public static final int PRECEDENCE = 200;
	public CategoryProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		String pid = tag.getAttributeValue(PID);
		final IEngineConfiguration configuration = context.getConfiguration();
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		/*
		 * Parse the attribute value as a Thymeleaf Standard
		 * Expression,通过表达式可以获取pid="${pid}" 变量中的值，否则直接获取pid="23"
		 */
		final IStandardExpression expression = parser.parseExpression(context, pid);
		Object pidObj = expression.execute(context);
		final Long parentId;
		if (pidObj instanceof BigInteger) {
			parentId = ((BigInteger) pidObj).longValue();
		} else {
			parentId = (Long) expression.execute(context);
		}
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		ArticleCategoryService categoryService = appCtx.getBean(ArticleCategoryService.class);
		List<ArticleCategory> categorys = categoryService.getChildCategory(parentId);
		structureHandler.setLocalVariable("categorys", categorys);
		structureHandler.removeTags();
	}

}

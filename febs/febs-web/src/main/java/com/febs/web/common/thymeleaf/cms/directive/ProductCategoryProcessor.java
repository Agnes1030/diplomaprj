package com.febs.web.common.thymeleaf.cms.directive;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Supplier;

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

import com.febs.common.domain.QueryRequest;
import com.febs.shop.domain.ProductCategory;
import com.febs.shop.service.ProductCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

public class ProductCategoryProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "productCategoryList";// 标签名
	private static final String PID = "pid";
	private static final String RECOMMEND="recommend";
	private static final String COUNT = "count";
	public static final int PRECEDENCE = 200;

	public ProductCategoryProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		final IEngineConfiguration configuration = context.getConfiguration();
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		ProductCategoryService categoryService = appCtx.getBean(ProductCategoryService.class);
		// 查询子分类
		String pid = tag.getAttributeValue(PID);
		String recommend = tag.getAttributeValue(RECOMMEND);
		String count = tag.getAttributeValue(COUNT);
		if (null != pid) {
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
			List<ProductCategory> categorys = categoryService.getChildProductCategory(parentId);
			structureHandler.setLocalVariable("categorys", categorys);
			structureHandler.removeTags();
		}
		if(null!=recommend) {
			Example example=new Example(ProductCategory.class);
			example.createCriteria().andEqualTo("recommend",recommend);
			QueryRequest request=new QueryRequest();
			request.setPageNum(1);
			if(null!=count) {
				request.setPageSize(Integer.parseInt(count));
			}
			PageInfo<?> pageInfo = this.selectByPageNumSize(request,
					() -> categoryService.selectByExample(example));
			structureHandler.setLocalVariable("categorys", pageInfo.getList());
			structureHandler.removeTags();
		}

	}
	protected PageInfo<?> selectByPageNumSize(QueryRequest request, Supplier<?> s) {
		PageHelper.startPage(request.getPageNum(), request.getPageSize());
		PageInfo<?> pageInfo = new PageInfo<>((List<?>) s.get());
		PageHelper.clearPage();
		return pageInfo;
	}
}

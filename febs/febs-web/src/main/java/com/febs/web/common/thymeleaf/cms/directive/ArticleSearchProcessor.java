package com.febs.web.common.thymeleaf.cms.directive;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.cms.domain.Article;
import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.service.ArticleService;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.common.domain.QueryRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 文章列表标签
 * @author 自由的路 
 * @date 2020-06-11
 */
public class ArticleSearchProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "articleSearchList";// 标签名
	private static final String PAGE_SIZE = "count";
	private static final String PAGE_NUM = "pageNum";
	private static final String KEYWORDS = "keywords";
	private static final String FIELD_CATEGORY = "categoryId";
	public static final int PRECEDENCE = 200;

	public ArticleSearchProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

//    <cms:articleSearchList categoryId="23" count="12">
//    <div th:each="article:${pageInfo.list}" >
//      <span th:text="${article.title}">Julius Caesar</span>
//    </cms>
//   </cms:articleSearchList>
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final IElementTagStructureHandler structureHandler) {
		Article article = new Article();
		// 分类ID列表
		List<Long> ids = new ArrayList<Long>();
		String categoryId = tag.getAttributeValue(FIELD_CATEGORY);
		final IEngineConfiguration configuration = context.getConfiguration();

		/*
		 * Obtain the Thymeleaf Standard Expression parser 获取Thymeleaf的表达式转换器
		 */
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		WebEngineContext ctx = (WebEngineContext) context;
		if (StringUtils.isNotBlank(categoryId)) {
			/*
			 * Parse the attribute value as a Thymeleaf Standard
			 * Expression,通过表达式可以获取categoryId="${category.id}" 变量中的值，否则直接获取categoryId="23"
			 */
			final IStandardExpression expression = parser.parseExpression(context, categoryId);

			/*
			 * Execute the expression just parsed 使用得到的表达式，处理上下文内容，得到具体传入的参数值 Demo中传入的是一个数字
			 */
			Object cateObj = expression.execute(context);
			final Long catId;
			if (cateObj instanceof BigInteger) {
				catId = ((BigInteger) cateObj).longValue();
			} else {
				catId = (Long) expression.execute(context);
			}
			ArticleCategoryService categoryService=appCtx.getBean(ArticleCategoryService.class);
			List<ArticleCategory> categorys = categoryService.findCategoryByPid(catId);
		
			for (ArticleCategory category : categorys) {
				ids.add(category.getId());
			}
		}

		String pageSize = tag.getAttributeValue(PAGE_SIZE);
		pageSize = pageSize == null ? "10" : pageSize;
		String pageNum = "1";
		ArticleService articleService = appCtx.getBean(ArticleService.class);
		HttpServletRequest request = ctx.getRequest();
		if (StringUtils.isNotEmpty(request.getParameter(PAGE_SIZE))) {
			pageSize = request.getParameter(PAGE_SIZE);
		}
		if (StringUtils.isNotEmpty(request.getParameter(PAGE_NUM))) {
			pageNum = request.getParameter(PAGE_NUM);
		}
		if (StringUtils.isNotEmpty(request.getParameter(KEYWORDS))) {
			String keywords = request.getParameter(KEYWORDS);
			article.setContent(keywords);
		}
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(Integer.parseInt(pageNum));
		queryRequest.setPageSize(Integer.parseInt(pageSize));

		PageInfo<?> pageInfo = this.selectByPageNumSize(queryRequest,
				() -> articleService.findAllArticles(article, ids));
		structureHandler.setLocalVariable("pageInfo", pageInfo);
		// 只清除标签本身，但内容还存在
		structureHandler.removeTags();
		// 整个元素都删除，内容也不存在
		// structureHandler.removeElement();
	}

	protected PageInfo<?> selectByPageNumSize(QueryRequest request, Supplier<?> s) {
		PageHelper.startPage(request.getPageNum(), request.getPageSize());
		PageInfo<?> pageInfo = new PageInfo<>((List<?>) s.get());
		PageHelper.clearPage();
		return pageInfo;
	}
}

package com.febs.web.common.thymeleaf.cms.directive;

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

import com.febs.common.domain.QueryRequest;
import com.febs.search.lucene.service.LuceneService;
import com.febs.shop.domain.Product;
import com.github.pagehelper.PageInfo;

/**
 * 搜索商品列表标签
 */
public class SearchProductProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "searchProductList";// 标签名
	private static final String PAGE_SIZE = "count";
	private static final String PAGE_NUM = "pageNum";
	private static final String CATEGORY_NAME = "categoryName";
	private static final String TITLE = "title";
	public static final int PRECEDENCE = 200;

	public SearchProductProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

// pageSize 和pageNum 都是可选参数
//    <cms:searchProductList categoryName="23" title="商品title" count="12">
//    <div th:each="product:${pageInfo.list}" >
//      <span th:text="${product.title}">Julius Caesar</span>
//    </div>
//   </cms:searchProductList>
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final IElementTagStructureHandler structureHandler) {
		String categoryName = tag.getAttributeValue(CATEGORY_NAME);
		String title = tag.getAttributeValue(TITLE);
		final IEngineConfiguration configuration = context.getConfiguration();

		/*
		 * Obtain the Thymeleaf Standard Expression parser 获取Thymeleaf的表达式转换器
		 */
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		WebEngineContext ctx = (WebEngineContext) context;
		/*
		 * Parse the attribute value as a Thymeleaf Standard
		 * Expression,通过表达式可以获取categoryId="${category.id}" 变量中的值，否则直接获取categoryId="23"
		 */
		final IStandardExpression expression = parser.parseExpression(context, categoryName);
		final IStandardExpression titleExpression = parser.parseExpression(context, title);
		/*
		 * Execute the expression just parsed 使用得到的表达式，处理上下文内容，得到具体传入的参数值 Demo中传入的是一个数字
		 */
		Object categoryObj = expression.execute(context);
		Object titleObj = titleExpression.execute(context);

		Product pro = new Product();

		if (null != categoryObj) {
			categoryName = categoryObj.toString();
			pro.setKeywords(categoryName);
		}
		if (null != titleObj) {
			title = titleObj.toString();
			pro.setTitle(title);
		}
		String pageSize = tag.getAttributeValue(PAGE_SIZE);
		pageSize = pageSize == null ? "10" : pageSize;
		String pageNum = "1";

		LuceneService luceneService = appCtx.getBean(LuceneService.class);
		HttpServletRequest request = ctx.getRequest();
		if (StringUtils.isNotEmpty(request.getParameter(PAGE_SIZE))) {
			pageSize = request.getParameter(PAGE_SIZE);
		}
		if (StringUtils.isNotEmpty(request.getParameter(PAGE_NUM))) {
			pageNum = request.getParameter(PAGE_NUM);
		}
		QueryRequest queryRequest = new QueryRequest();

		queryRequest.setPageNum(Integer.parseInt(pageNum));
		queryRequest.setPageSize(Integer.parseInt(pageSize));
		PageInfo<?> pageInfo;
		try {
			pageInfo = luceneService.searchProduct(queryRequest, pro);
			structureHandler.setLocalVariable("pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 只清除标签本身，但内容还存在
		structureHandler.removeTags();
		// 整个元素都删除，内容也不存在
		// structureHandler.removeElement();
	}

}

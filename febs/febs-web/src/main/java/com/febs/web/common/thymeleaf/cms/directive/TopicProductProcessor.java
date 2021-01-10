package com.febs.web.common.thymeleaf.cms.directive;

import java.math.BigInteger;
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

import com.febs.common.domain.QueryRequest;
import com.febs.shop.service.ProductTopicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TopicProductProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "topicProductList";// 标签名
	private static final String PAGE_SIZE = "count";
	private static final String PAGE_NUM = "pageNum";
	private static final String FIELD_TOPIC = "topicId";
	public static final int PRECEDENCE = 200;

	public TopicProductProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

// pageSize 和pageNum 都是可选参数
//    <cms:topicProductList topicId="23" count="12">
//    <div th:each="product:${pageInfo.list}" >
//      <span th:text="${product.title}">Julius Caesar</span>
//    </div>
//   </cms:topicProductList>
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final IElementTagStructureHandler structureHandler) {
		String topicIdAttr = tag.getAttributeValue(FIELD_TOPIC);
		final IEngineConfiguration configuration = context.getConfiguration();

		/*
		 * Obtain the Thymeleaf Standard Expression parser 获取Thymeleaf的表达式转换器
		 */
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		WebEngineContext ctx = (WebEngineContext) context;
		Long topicId;
		/*
		 * Parse the attribute value as a Thymeleaf Standard
		 * Expression,通过表达式可以获取categoryId="${category.id}" 变量中的值，否则直接获取categoryId="23"
		 */
		final IStandardExpression expression = parser.parseExpression(context, topicIdAttr);

		/*
		 * Execute the expression just parsed 使用得到的表达式，处理上下文内容，得到具体传入的参数值 Demo中传入的是一个数字
		 */
		Object topicIdAttrObj = expression.execute(context);

		if (topicIdAttrObj instanceof BigInteger) {
			topicId = ((BigInteger) topicIdAttrObj).longValue();
		} else {
			topicId = (Long) expression.execute(context);
		}

		String pageSize = tag.getAttributeValue(PAGE_SIZE);
		pageSize = pageSize == null ? "10" : pageSize;
		String pageNum = "1";

		ProductTopicService topicService = appCtx.getBean(ProductTopicService.class);
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
		PageInfo<?> pageInfo = this.selectByPageNumSize(queryRequest, () -> topicService.getTopicProducts(topicId));
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

package com.febs.web.common.thymeleaf.cms.directive;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.common.domain.QueryRequest;
import com.febs.shop.domain.ProductTopic;
import com.febs.shop.service.ProductTopicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

public class RecommendTopicProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "recommendTopic";// 标签名
	private static final String COUNT = "count";
	public static final int PRECEDENCE = 200;

	public RecommendTopicProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

// pageSize 和pageNum 都是可选参数
//    <cms:recommendTopic count="12">
//    <div th:each="topic:${topics}" >
//      <span th:text="${topic.name}">Julius Caesar</span>
//    </div>
//  </cms:recommendTopic>
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final IElementTagStructureHandler structureHandler) {
		String pageSize = tag.getAttributeValue(COUNT);
		String pageNum = tag.getAttributeValue("1");
		pageSize = pageSize == null ? "10" : pageSize;
		pageNum = pageNum == null ? "1" : pageNum;
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		ProductTopicService topicService = appCtx.getBean(ProductTopicService.class);
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(Integer.parseInt(pageNum));
		queryRequest.setPageSize(Integer.parseInt(pageSize));
		Example topicExample = new Example(ProductTopic.class);
		topicExample.createCriteria().andEqualTo("recommend", "1");
		PageInfo<?> pageInfo = this.selectByPageNumSize(queryRequest, () -> topicService.selectByExample(topicExample));
		structureHandler.setLocalVariable("topics", pageInfo.getList());
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

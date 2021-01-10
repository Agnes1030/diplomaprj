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

import com.febs.cms.domain.Article;
import com.febs.cms.service.ArticleService;
import com.febs.common.domain.QueryRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
/**
 * 热门文章列表标签
 * @author 自由的路 
 * @date 2020-06-11
 */
public class HotArticleProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "hotArticles";// 标签名
	private static final String COUNT = "count";
	public static final int PRECEDENCE = 200;

	public HotArticleProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

//    <cms:hotArticles  count="12">
//    <div th:each="article:${hotArticles}" >
//      <span th:text="${article.title}">Julius Caesar</span>
//    </div>
//  </cms:hotArticles>
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final IElementTagStructureHandler structureHandler) {
		String pageSize = tag.getAttributeValue(COUNT);
		String pageNum = tag.getAttributeValue("1");
		pageSize = pageSize == null ? "10" : pageSize;
		pageNum = pageNum == null ? "1" : pageNum;
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		// WebEngineContext ctx = (WebEngineContext) context;
		ArticleService articleService = appCtx.getBean(ArticleService.class);
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(Integer.parseInt(pageNum));
		queryRequest.setPageSize(Integer.parseInt(pageSize));
		Example example = new Example(Article.class);
		example.orderBy("viewCount").desc();
		PageInfo<?> pageInfo = this.selectByPageNumSize(queryRequest, () -> articleService.selectByExample(example));
		structureHandler.setLocalVariable("hotArticles", pageInfo.getList());
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

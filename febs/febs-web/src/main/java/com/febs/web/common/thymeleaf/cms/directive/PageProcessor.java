package com.febs.web.common.thymeleaf.cms.directive;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.cms.domain.SinglePage;
import com.febs.cms.service.SinglePageService;
/**
 * 单页标签
 * @author 自由的路 
 * @date 2020-06-11
 */
public class PageProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "singlePage";// 标签名
	private static final String PAGE_ID= "pageId";
	public static final int PRECEDENCE = 200;
	public PageProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		String pageId = tag.getAttributeValue(PAGE_ID);
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		SinglePageService singlePageService = appCtx.getBean(SinglePageService.class);
		Long singlePageId = Long.parseLong(pageId);
		SinglePage singlePage = singlePageService.findById(singlePageId);
		structureHandler.setLocalVariable("singlePage",singlePage);
		structureHandler.removeTags();

	}

}

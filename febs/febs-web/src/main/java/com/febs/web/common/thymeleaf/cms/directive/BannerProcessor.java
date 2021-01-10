package com.febs.web.common.thymeleaf.cms.directive;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.cms.domain.Banner;
import com.febs.cms.service.BannerService;
/**
 * Banner列表标签
 * @author 自由的路 
 * @date 2020-06-11
 */
public class BannerProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "banners";// 标签名
	public static final int PRECEDENCE = 200;

	public BannerProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		final IEngineConfiguration configuration = context.getConfiguration();
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		WebEngineContext ctx = (WebEngineContext) context;
		BannerService bannerService = appCtx.getBean(BannerService.class);
		HttpServletRequest request = ctx.getRequest();
		List<Banner> banners = bannerService.getAllBanners();
		structureHandler.setLocalVariable("banners", banners);
		// 只清除标签本身，但内容还存在
		structureHandler.removeTags();
		// 整个元素都删除，内容也不存在
		//structureHandler.removeElement();
	}

}

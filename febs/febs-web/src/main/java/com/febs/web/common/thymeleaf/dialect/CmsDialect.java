package com.febs.web.common.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import com.febs.web.common.thymeleaf.cms.directive.ArticleProcessor;
import com.febs.web.common.thymeleaf.cms.directive.ArticleSearchProcessor;
import com.febs.web.common.thymeleaf.cms.directive.BannerProcessor;
import com.febs.web.common.thymeleaf.cms.directive.CategoryProcessor;
import com.febs.web.common.thymeleaf.cms.directive.HasPermissonProcessor;
import com.febs.web.common.thymeleaf.cms.directive.HotArticleProcessor;
import com.febs.web.common.thymeleaf.cms.directive.HotProductProcessor;
import com.febs.web.common.thymeleaf.cms.directive.PageProcessor;
import com.febs.web.common.thymeleaf.cms.directive.ProductCategoryProcessor;
import com.febs.web.common.thymeleaf.cms.directive.ProductImageProcessor;
import com.febs.web.common.thymeleaf.cms.directive.ProductProcessor;
import com.febs.web.common.thymeleaf.cms.directive.RecommendTopicProcessor;
import com.febs.web.common.thymeleaf.cms.directive.SearchProductProcessor;
import com.febs.web.common.thymeleaf.cms.directive.ShopCartProcessor;
import com.febs.web.common.thymeleaf.cms.directive.TagArticleProcessor;
import com.febs.web.common.thymeleaf.cms.directive.TopicProductProcessor;
import com.febs.web.common.thymeleaf.cms.directive.UserMenuProcessor;

public class CmsDialect extends AbstractProcessorDialect {
	private static final String NAME = "cms dialect";
	private static final String PREFIX = "cms";

	public CmsDialect() {
		super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		return createStandardProcessorsSet(dialectPrefix);
	}

	private static Set<IProcessor> createStandardProcessorsSet(String dialectPrefix) {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new ArticleProcessor(dialectPrefix));
		processors.add(new ProductProcessor(dialectPrefix));
		processors.add(new TagArticleProcessor(dialectPrefix));
		processors.add(new BannerProcessor(dialectPrefix));
		processors.add(new HotArticleProcessor(dialectPrefix));
		processors.add(new ArticleSearchProcessor(dialectPrefix));
		processors.add(new PageProcessor(dialectPrefix));
		processors.add(new CategoryProcessor(dialectPrefix));
		processors.add(new ProductCategoryProcessor(dialectPrefix));
		processors.add(new ProductImageProcessor(dialectPrefix));
		processors.add(new TopicProductProcessor(dialectPrefix));
		processors.add(new RecommendTopicProcessor(dialectPrefix));
		processors.add(new HotProductProcessor(dialectPrefix));
		processors.add(new ShopCartProcessor(dialectPrefix));
		processors.add(new SearchProductProcessor(dialectPrefix));
		processors.add(new UserMenuProcessor(dialectPrefix));

		processors.add(new HasPermissonProcessor(TemplateMode.HTML, dialectPrefix));
		return processors;
	}
}

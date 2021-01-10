package com.febs.web.common.thymeleaf.cms.directive;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import com.febs.shop.domain.ProductImage;
import com.febs.shop.service.ProductImageService;
/**
 * 产品图片列表标签
 * @author 自由的路 
 * @date 2020-06-11
 */
public class ProductImageProcessor extends AbstractElementTagProcessor {
	private static final String TAG_NAME = "productImgList";// 标签名
	private static final String PRODUCTID = "productId";
	public static final int PRECEDENCE = 200;

	public ProductImageProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		// 分类ID列表
		String productIdAttr = tag.getAttributeValue(PRODUCTID);
		final IEngineConfiguration configuration = context.getConfiguration();

		/*
		 * Obtain the Thymeleaf Standard Expression parser 获取Thymeleaf的表达式转换器
		 */
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);
		if (StringUtils.isNotBlank(productIdAttr)) {
			/*
			 * Parse the attribute value as a Thymeleaf Standard
			 * Expression,通过表达式可以获取categoryId="${category.id}" 变量中的值，否则直接获取categoryId="23"
			 */
			final IStandardExpression expression = parser.parseExpression(context, productIdAttr);

			/*
			 * Execute the expression just parsed 使用得到的表达式，处理上下文内容，得到具体传入的参数值 Demo中传入的是一个数字
			 */
			Object imgObj = expression.execute(context);
			final Long productId;
			if (imgObj instanceof BigInteger) {
				productId = ((BigInteger) imgObj).longValue();
			} else {
				productId = (Long) expression.execute(context);
			}
			ProductImageService productImageService = appCtx.getBean(ProductImageService.class);
			List<ProductImage> productImgs = productImageService.getProductImgs(productId);
			structureHandler.setLocalVariable("productImages", productImgs);
			// 只清除标签本身，但内容还存在
			structureHandler.removeTags();
			// 整个元素都删除，内容也不存在
			// structureHandler.removeElement();

		}

	}

}

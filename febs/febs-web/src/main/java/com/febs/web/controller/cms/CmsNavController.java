package com.febs.web.controller.cms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.febs.cms.domain.Article;
import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.domain.ArticleTag;
import com.febs.cms.domain.NavMenu;
import com.febs.cms.domain.Setting;
import com.febs.cms.domain.SinglePage;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.cms.service.ArticleService;
import com.febs.cms.service.ArticleTagService;
import com.febs.cms.service.NavMenuService;
import com.febs.cms.service.SettingService;
import com.febs.cms.service.SinglePageService;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.Tree;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductCategory;
import com.febs.shop.domain.ProductImage;
import com.febs.shop.domain.ProductSku;
import com.febs.shop.domain.ProductSpecification;
import com.febs.shop.domain.ProductSpecificationValue;
import com.febs.shop.domain.ProductTopic;
import com.febs.shop.service.ProductCategoryService;
import com.febs.shop.service.ProductImageService;
import com.febs.shop.service.ProductService;
import com.febs.shop.service.ProductSkuService;
import com.febs.shop.service.ProductSpecificationService;
import com.febs.shop.service.ProductSpecificationValueService;
import com.febs.shop.service.ProductTopicService;
import com.febs.web.controller.admin.shop.vo.SpecSelectVO;
import com.febs.web.controller.base.WebBaseController;
import com.google.gson.Gson;

import tk.mybatis.mapper.entity.Example;

@Controller
public class CmsNavController extends WebBaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SettingService settingService;
	@Autowired
	private NavMenuService navMenuService;
	@Autowired
	private ArticleCategoryService articleCategoryService;
	@Autowired
	private SinglePageService singlePageService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleTagService articleTagService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductSpecificationService productSpecificationService;
	@Autowired
	private ProductSkuService productSkuService;
	@Autowired
	private ProductTopicService productTopicService;
	@Autowired
	private ProductSpecificationValueService productSpecificationValueService;
	@Autowired
	private ProductImageService productImageService;

	/**
	 * 文章列表
	 */
	@RequestMapping(value = "/articleCategory/{id_slug}")
	public String categoryList(Model model, QueryRequest request, @PathVariable("id_slug") String idSlug) {
		log.info("slug=" + idSlug);
		ArticleCategory category = articleCategoryService.findByIdSlug(idSlug);
		Setting setting = settingService.findByKey("theme");
		if (null == category) {
			return "error/404.html";
		}
		// 递归查询当前目录的子目录用于在列表页显示二级分类列表等
		Tree<ArticleCategory> tree = articleCategoryService.getCategoryChildTree(category);
		NavMenu navMenu = navMenuService.findNavByRelation("category", category.getId());
		if (null != navMenu) {
			navMenuService.setActiveNav(navMenu.getId().toString());
		}
		model.addAttribute("childs", tree.getChildren());
		model.addAttribute("category", category);
		model.addAttribute("navMenu", navMenu);
		return "themes/" + setting.getValue() + "/" + category.getTemplate();
	}

	/**
	 * 商品列表
	 */
	@RequestMapping(value = "/productCategory/{id_slug}")
	public String productCategoryList(Model model, QueryRequest request, @PathVariable("id_slug") String idSlug) {
		log.info("slug=" + idSlug);
		ProductCategory category = productCategoryService.findByIdSlug(idSlug);
		Setting setting = settingService.findByKey("theme");
		if (null == category) {
			return "error/404.html";
		}
		// 递归查询当前目录的子目录用于在列表页显示二级分类列表等
		Tree<ProductCategory> tree = productCategoryService.getProductCategoryChildTree(category);
		NavMenu navMenu = navMenuService.findNavByRelation("category", category.getId());
		if (null != navMenu) {
			navMenuService.setActiveNav(navMenu.getId().toString());
			model.addAttribute("navMenu", navMenu);
		}
		model.addAttribute("childs", tree.getChildren());
		model.addAttribute("category", category);
		return "themes/" + setting.getValue() + "/" + category.getTemplate();
	}

	/**
	 * 商品列表
	 */
	@RequestMapping(value = "/productTopic/{id_slug}")
	public String productTopicList(Model model, QueryRequest request, @PathVariable("id_slug") String idSlug) {
		log.info("slug=" + idSlug);
		boolean isNum = StringUtils.isNumeric(idSlug);
		ProductTopic topic = null;
		if (isNum) {
			topic = productTopicService.selectByKey(idSlug);
		} else {
			topic = productTopicService.findBySlug(idSlug);
		}
		Setting setting = settingService.findByKey("theme");
		// 递归查询当前目录的子目录用于在列表页显示二级分类列表等
		NavMenu navMenu = navMenuService.findNavByRelation("topic", topic.getId());
		if (null != navMenu) {
			navMenuService.setActiveNav(navMenu.getId().toString());
			model.addAttribute("navMenu", navMenu);
		}
		model.addAttribute("topic", topic);

		return "themes/" + setting.getValue() + "/" + topic.getTemplate();
	}

	@RequestMapping(value = "/page/{id_slug}")
	public String singleNavPage(Model model, @PathVariable("id_slug") String idSlug) {
		boolean isNum = StringUtils.isNumeric(idSlug);
		SinglePage singlePage = null;
		if (isNum) {
			singlePage = singlePageService.selectByKey(idSlug);
		} else {
			singlePage = singlePageService.findBySlug(idSlug);
		}
		Setting setting = settingService.findByKey("theme");
		NavMenu navMenu = navMenuService.findNavByRelation("page", singlePage.getId());
		if (null != navMenu) {
			navMenuService.setActiveNav(navMenu.getId().toString());
		}
		model.addAttribute("singlePage", singlePage);
		return "themes/" + setting.getValue() + "/" + singlePage.getTemplate();
	}

	/**
	 * 文章详情(如果是从通过分类导航过来的，就要查询出对应的分类信息，所以在详情页要判断分类信息是否为空)
	 */
	@RequestMapping(value = "/article/view/{id_slug}")
	public String viewArticle(HttpServletRequest request, Model model, @PathVariable("id_slug") String idSlug) {
		// 如果是查看源码过来的请求，referer为null
		String fromURL = request.getHeader("Referer");
		String categoryStr = "/category/";
		if (null != fromURL && fromURL.indexOf(categoryStr) > -1) {
			int beginIndex = fromURL.lastIndexOf(categoryStr);
			String categoryIdSlug = fromURL.substring(beginIndex + categoryStr.length());
			ArticleCategory category = articleCategoryService.findByIdSlug(categoryIdSlug);
			NavMenu navMenu = navMenuService.findNavByRelation("category", category.getId());
			if (null != navMenu) {
				navMenuService.setActiveNav(navMenu.getId().toString());
			}
			model.addAttribute("navMenu", navMenu);
			model.addAttribute("category", category);
		}
		Setting setting = settingService.findByKey("theme");
		Article article = articleService.findByIdSlug(idSlug);
		if (null == article) {
			return "themes/" + setting.getValue() + "/" + "/404.html";
		}
		model.addAttribute("article", article);
		// 更新阅读数
		articleService.updateViewCount(article.getId());
		return "themes/" + setting.getValue() + "/" + article.getTemplate();
	}

	/**
	 * 商品详情(如果是从通过分类导航过来的，就要查询出对应的分类信息，所以在详情页要判断分类信息是否为空)
	 */
	@RequestMapping(value = "/product/view/{id_slug}")
	public String viewProduct(HttpServletRequest request, Model model, @PathVariable("id_slug") String idSlug) {
		// 如果是查看源码过来的请求，referer为null
		String fromURL = request.getHeader("Referer");
		// 判断是否为分类跳转的处理
		String categoryStr = "/productCategory/";
		if (null != fromURL && fromURL.indexOf(categoryStr) > -1) {
			int beginIndex = fromURL.lastIndexOf(categoryStr);
			String categoryIdSlug = fromURL.substring(beginIndex + categoryStr.length());
			ProductCategory category = productCategoryService.findByIdSlug(categoryIdSlug);
			NavMenu navMenu = navMenuService.findNavByRelation("category", category.getId());
			if (null != navMenu) {
				navMenuService.setActiveNav(navMenu.getId().toString());
			}
			model.addAttribute("navMenu", navMenu);
			model.addAttribute("category", category);
		}
		// 判断是否为专题页跳转的处理
		String topicStr = "/topic/";
		if (null != fromURL && fromURL.indexOf(topicStr) > -1) {
			int beginIndex = fromURL.lastIndexOf(categoryStr);
			String topicIdSlug = fromURL.substring(beginIndex + categoryStr.length());
			ProductTopic topic = productTopicService.findByIdSlug(topicIdSlug);
			NavMenu navMenu = navMenuService.findNavByRelation("topic", topic.getId());
			if (null != navMenu) {
				navMenuService.setActiveNav(navMenu.getId().toString());
			}
			model.addAttribute("navMenu", navMenu);
			model.addAttribute("topic", topic);

		}
		Product product = productService.findByIdSlug(idSlug);
		Setting setting = settingService.findByKey("theme");

		if (null == product) {
			return "error/404.html";
		}
		Long productId = product.getId();
		List<ProductImage> images = this.productImageService.getProductImgs(productId);
		product.setProductImages(images);
		Example specExample = new Example(ProductSpecificationValue.class);
		specExample.createCriteria().andEqualTo("productId", productId);
		specExample.setOrderByClause("'seq_num' asc");
		List<ProductSpecification> specList = this.productSpecificationService.selectByExample(specExample);
		List<SpecSelectVO> specSelectList = new ArrayList<SpecSelectVO>();
		for (ProductSpecification productSpec : specList) {
			SpecSelectVO specVO = new SpecSelectVO();
			specVO.setSpecId(productSpec.getId());
			specVO.setSpecName(productSpec.getSpecification());
			// 查询规格对应的值列表
			Example specValueExample = new Example(ProductSpecificationValue.class);
			specValueExample.createCriteria().andEqualTo("specificationId", productSpec.getId());
			List<ProductSpecificationValue> values = productSpecificationValueService.selectByExample(specValueExample);
			specVO.setValues(values);
			specSelectList.add(specVO);
		}
		specExample.createCriteria().andEqualTo("productId", productId);
		Example skuExample = new Example(ProductSku.class);
		skuExample.createCriteria().andEqualTo("productId", productId);
		List<ProductSku> skuList = this.productSkuService.selectByExample(skuExample);
		Gson gson = new Gson();
		String skuListJson = gson.toJson(skuList);
		String specSelectListJson = gson.toJson(specSelectList);
		model.addAttribute("skuList", skuList);
		model.addAttribute("skuListJson", skuListJson);
		model.addAttribute("specSelectList", specSelectList);
		model.addAttribute("specSelectListJson", specSelectListJson);
		model.addAttribute("specList", specList);
		model.addAttribute("product", product);
		// 更新阅读数
		productService.updateViewCount(productId);
		return "themes/" + setting.getValue() + "/" + product.getTemplate();
	}

	/**
	 * 查看标签文章列表
	 * 
	 * @param model
	 * @param request
	 * @param article
	 * @param tagName
	 * @return
	 */
	@RequestMapping(value = "/tags/{tagName}")
	public String tagList(Model model, QueryRequest request, Article article, @PathVariable("tagName") String tagName) {
		log.info("tagName=" + tagName);
		Setting setting = settingService.findByKey("theme");
		List<ArticleTag> tags = articleTagService.getTagByName(tagName);
		ArticleTag articleTag = tags.get(0);
		model.addAttribute("articleTag", articleTag);
		return "themes/" + setting.getValue() + "/" + articleTag.getTemplate();
	}

	/**
	 * 查看标签列表文章
	 * 
	 * @param model
	 * @param tagName
	 * @param articleId
	 * @return
	 */
	@RequestMapping(value = "/{tagName}/tag/{articleId}")
	public String vieTagArticle(Model model, @PathVariable("tagName") String tagName,
			@PathVariable("articleId") Long articleId) {
		Setting setting = settingService.findByKey("theme");
		List<ArticleTag> tags = articleTagService.getTagByName(tagName);
		ArticleTag articleTag = tags.get(0);
		Article article = articleService.findById(articleId);
		if (null == article) {
			return "error/404.html";
		}
		model.addAttribute("article", article);
		model.addAttribute("articleTag", articleTag);
		// 更新阅读数
		articleService.updateViewCount(articleId);
		return "themes/" + setting.getValue() + "/" + article.getTemplate();
	}
}

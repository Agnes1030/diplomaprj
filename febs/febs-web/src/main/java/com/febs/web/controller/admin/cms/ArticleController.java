package com.febs.web.controller.admin.cms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.Article;
import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.domain.ArticleCategoryMapping;
import com.febs.cms.domain.ArticleTag;
import com.febs.cms.domain.ArticleTagMapping;
import com.febs.cms.service.ArticleCategoryMappingService;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.cms.service.ArticleService;
import com.febs.cms.service.ArticleTagMappingService;
import com.febs.cms.service.ArticleTagService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;

import tk.mybatis.mapper.entity.Example;

/**
 * 后台文章管理controller
 * 
 * @author wtsoftware
 * @date 2020-06-14
 */
@Controller
@RequestMapping("/admin")
public class ArticleController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleCategoryService categoryService;
	@Autowired
	private ArticleTagService articleTagService;
	@Autowired
	private ArticleTagMappingService articleTagMappingService;
	@Autowired
	private ArticleCategoryMappingService articleCategoryService;

	@Log("文章列表")
	@RequestMapping("/article")
	@PreAuthorize("hasAuthority('article:list')")
	public String index() {
		return "admin/cms/article/article";
	}

	@Log("获取文章列表信息")
	@RequestMapping("/article/list")
	@PreAuthorize("hasAuthority('article:list')")
	@ResponseBody
	public Map<String, Object> articleList(HttpServletRequest request, QueryRequest queryRequest, Article article) {
		if (null == request.getParameter("categoryId")) {
			Example example = new Example(Article.class);
			if (StringUtils.isNotBlank(article.getTitle())) {
				example.createCriteria().andLike("title", "%" + article.getTitle() + "%");
			}
			example.setOrderByClause("id desc");
			return super.selectByPageNumSize(queryRequest, () -> this.articleService.selectByExample(example));
		}
		Long categoryId = Long.parseLong(request.getParameter("categoryId").toString());
		List<Long> ids = new ArrayList<Long>();
		ids.add(categoryId);
		return super.selectByPageNumSize(queryRequest, () -> this.articleService.findAllArticles(article, ids));
	}

	@RequestMapping("/article/getArticle")
	@ResponseBody
	public ResponseBo getArticle(Long articleId) {
		try {
			Article article = this.articleService.findById(articleId);
			return ResponseBo.ok(article);
		} catch (Exception e) {
			log.error("获取文章信息失败", e);
			return ResponseBo.error("获取文章信息失败，请联系网站管理员！");
		}
	}
	@GetMapping("/article/add")
	public String toAddArticle(HttpServletRequest request) {
		request.setAttribute("action","save");
		return "admin/cms/article/articleAdd";
	}
	@Log("新增文章 ")
	@PreAuthorize("hasAuthority('article:add')")
	@PostMapping("/article/add")
	@ResponseBody
	public ResponseBo addArticle(Article article, HttpServletRequest request) {
		try {
			if (StringUtils.isNotEmpty(article.getSlug())) {
				String slug = article.getSlug();
				Example example = new Example(Article.class);
				example.selectProperties("slug").createCriteria().andEqualTo("slug", slug);
				Article slugArticle = articleService.selectOneByExample(example);
				if (null != slugArticle) {
					return ResponseBo.error("slug名称重复");
				}
			}

			MyUser currentUser = this.getCurrentUser();
			article.setAuthor(currentUser.getUsername());
			article.setUserId(currentUser.getUserId());
			this.articleService.addArticle(article);
			Long articleId = article.getId();
			// 分类相关处理
			String catIds = article.getCategoryIds();
			String[] categoryIds = catIds.split(",");
			for (String catId : categoryIds) {
				Long categoryId = Long.parseLong(catId);
				ArticleCategoryMapping articleCategoryMapping = new ArticleCategoryMapping();
				articleCategoryMapping.setArticleId(articleId);
				articleCategoryMapping.setCategoryId(categoryId);
				articleCategoryService.save(articleCategoryMapping);
			}
			// tag 相关处理
			String tagIds = article.getTags();
			String[] tagNames = tagIds.split(",");
			for (String tagName : tagNames) {
				ArticleTag articleTag = new ArticleTag();
				List<ArticleTag> tags = articleTagService.getTagByName(tagName);
				if (null == tags || tags.size() == 0) {
					articleTag.setTagName(tagName);
					articleTag.setCounts(1);
					articleTag.setTemplate("tagArticleList.html");
					articleTagService.addArticleTag(articleTag);
				} else {
					articleTag = tags.get(0);
				}
				int counts = articleTag.getCounts();
				articleTag.setCounts(counts + 1);
				articleTagService.updateArticleTag(articleTag);
				ArticleTagMapping articleTagMapping = new ArticleTagMapping();
				articleTagMapping.setArticleId(articleId);
				articleTagMapping.setTagId(articleTag.getId());
				articleTagMappingService.save(articleTagMapping);
			}
			return ResponseBo.ok("新增文章成功！");
		} catch (Exception e) {
			log.error("新增文章失败", e);
			return ResponseBo.error("新增文章失败，请联系网站管理员！");
		}
	}
	@GetMapping("/article/update")
	public String toEditArticle(HttpServletRequest request) {
		String articleId = request.getParameter("articleId");
		request.setAttribute("action","update");
		request.setAttribute("articleId", articleId);
		return "admin/cms/article/articleAdd";
	}
	@Log("修改文章 ")
	@PreAuthorize("hasAuthority('article:update')")
	@PostMapping("/article/update")
	@ResponseBody
	public ResponseBo updateArticle(Article article, HttpServletRequest request) {
		try {
			if (StringUtils.isNotEmpty(article.getSlug())) {
				String slug = article.getSlug();
				Example example = new Example(Article.class);
				example.selectProperties("id","slug").createCriteria().andEqualTo("slug", slug);
				Article slugArticle = articleService.selectOneByExample(example);
				if (null != slugArticle && slugArticle.getId().longValue()!=article.getId().longValue()) {
					return ResponseBo.error("slug名称重复");
				}
			}
			this.articleService.updateArticle(article);
			Long articleId = article.getId();
			// 分类相关处理
			this.articleCategoryService.deleteArticleCategoryMapping(articleId);
			String categoryIdsStr = article.getCategoryIds();
			String[] categoryIds = categoryIdsStr.split(",");
			for (String categoryIdStr : categoryIds) {
				Long categoryId = Long.parseLong(categoryIdStr);
				ArticleCategoryMapping articleCategoryMapping = new ArticleCategoryMapping();
				articleCategoryMapping.setArticleId(articleId);
				articleCategoryMapping.setCategoryId(categoryId);
				articleCategoryService.save(articleCategoryMapping);
			}
			// 先删除原有引用关系，再重新创建
			List<ArticleTag> originTags = articleTagService.getTagByArticleIds(articleId.toString());
			for (ArticleTag articleTag : originTags) {
				if (null != articleTag) {
					int counts = articleTag.getCounts();
					articleTag.setCounts(counts - 1);
					articleTagService.updateArticleTag(articleTag);
				}
			}
			this.articleTagMappingService.deleteArticleTagMapping(articleId);

			String tagsStr = request.getParameter("tags");
			String[] tagNames = tagsStr.split(",");
			for (String tagName : tagNames) {
				ArticleTag articleTag = new ArticleTag();
				List<ArticleTag> tags = articleTagService.getTagByName(tagName);
				if (null == tags || tags.size() == 0) {
					articleTag.setTagName(tagName);
					articleTag.setCounts(1);
					articleTag.setTemplate("tagArticleList.html");
					articleTagService.addArticleTag(articleTag);
				} else {
					articleTag = tags.get(0);
				}
				ArticleTagMapping articleTagMapping = new ArticleTagMapping();
				articleTagMapping.setArticleId(articleId);
				articleTagMapping.setTagId(articleTag.getId());
				articleTagMappingService.save(articleTagMapping);
			}
			return ResponseBo.ok("修改文章成功！");
		} catch (Exception e) {
			log.error("修改文章失败", e);
			return ResponseBo.error("修改文章失败，请联系网站管理员！");
		}
	}

	@Log("删除文章")
	@PreAuthorize("hasAuthority('article:delete')")
	@RequestMapping("/article/delete")
	@ResponseBody
	public ResponseBo deleteArticles(String ids) {
		try {
			this.articleService.deleteArticles(ids);
			this.articleCategoryService.batchDeleteCategoryMapping(ids);
			this.articleTagMappingService.batchDeleteTagMapping(ids);
			List<ArticleTag> tags = articleTagService.getTagByArticleIds(ids);
			// 减少tag关联的文章数量
			for (ArticleTag articleTag : tags) {
				int counts = articleTag.getCounts();
				articleTag.setCounts(counts - 1);
				articleTagService.updateArticleTag(articleTag);
			}
			return ResponseBo.ok("删除文章成功！");
		} catch (Exception e) {
			log.error("删除文章失败", e);
			return ResponseBo.error("删除文章失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/article/excel")
	@ResponseBody
	public ResponseBo articleExcel(Article article, HttpServletRequest request) {
		try {
			String categoryIdStr = request.getParameter("categoryId");
			Long categoryId = Long.parseLong(categoryIdStr);
			List<ArticleCategory> categorys = categoryService.findCategoryByPid(categoryId);
			List<Long> ids = new ArrayList<Long>();
			for (ArticleCategory category : categorys) {
				ids.add(category.getId());
			}
			List<Article> list = this.articleService.findAllArticles(article, ids);
			return FileUtils.createExcelByPOIKit("文章表", list, Article.class);
		} catch (Exception e) {
			log.error("导出文章信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/article/csv")
	@ResponseBody
	public ResponseBo articleCsv(Article article, HttpServletRequest request) {
		try {
			String categoryIdStr = request.getParameter("categoryId");
			Long categoryId = Long.parseLong(categoryIdStr);
			List<ArticleCategory> categorys = categoryService.findCategoryByPid(categoryId);
			List<Long> ids = new ArrayList<Long>();
			for (ArticleCategory category : categorys) {
				ids.add(category.getId());
			}
			List<Article> list = this.articleService.findAllArticles(article, ids);
			return FileUtils.createCsv("文章表", list, Article.class);
		} catch (Exception e) {
			log.error("获取文章信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

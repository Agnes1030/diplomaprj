package com.febs.web.controller.member;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.Article;
import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.domain.ArticleCategoryMapping;
import com.febs.cms.domain.ArticleComment;
import com.febs.cms.domain.ArticleTag;
import com.febs.cms.domain.ArticleTagMapping;
import com.febs.cms.service.ArticleCategoryMappingService;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.cms.service.ArticleCommentService;
import com.febs.cms.service.ArticleService;
import com.febs.cms.service.ArticleTagMappingService;
import com.febs.cms.service.ArticleTagService;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.domain.Tree;
import com.febs.member.domain.UserFavourite;
import com.febs.member.service.UserFavouriteService;
import com.febs.shop.domain.Product;
import com.febs.shop.domain.ProductComment;
import com.febs.shop.service.ProductCommentService;
import com.febs.shop.service.ProductService;
import com.febs.system.domain.MyUser;
import com.febs.system.service.UserService;
import com.febs.web.controller.base.BaseController;

import redis.clients.jedis.JedisPool;
import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	@Autowired
	private ArticleTagService articleTagService;
	@Autowired
	private ArticleTagMappingService articleTagMappingService;
	@Autowired
	private ArticleCategoryMappingService articleCategoryService;
	@Autowired
	private ArticleCategoryService categoryService;
	@Autowired
	JedisPool jedisPool;
	@Autowired
	private ArticleCommentService articleCommentService;
	@Autowired
	private ProductCommentService productCommentService;
	@Autowired
	private UserFavouriteService userFavouriteService;
	@Autowired
	private ProductService productService;

	@RequestMapping("favouritesIndex")
	public String collectIndex() {
		return "member/userFavourites";
	}

	@RequestMapping("favourites/list")
	@ResponseBody
	public Map<String, Object> favouriteList(QueryRequest request) {
		MyUser myUser = this.getCurrentUser();
		Example favouriteExample = new Example(UserFavourite.class);
		favouriteExample.createCriteria().andEqualTo("userId", myUser.getUserId());
		return super.selectByPageNumSize(request, () -> userFavouriteService.selectByExample(favouriteExample));
	}

	/**
	 * 新增收藏文章
	 * 
	 * @param articleId 文章ID
	 */
	@RequestMapping("favourites/addArticleFavourite")
	@ResponseBody
	public ResponseBo addArticleFavourite(Long articleId) {
		Article article = articleService.selectByKey(articleId);
		if (null == article) {
			ResponseBo.error();
		}
		MyUser myUser = this.getCurrentUser();
		// 判断是否已经收藏
		Example favouriteExample = new Example(UserFavourite.class);
		favouriteExample.createCriteria().andEqualTo("userId", myUser.getUserId()).andEqualTo("type", "article")
				.andEqualTo("favouriteId", articleId);
		List<UserFavourite> userFavourites = userFavouriteService.selectByExample(favouriteExample);
		if (null != userFavourites && userFavourites.size() > 0) {
			return ResponseBo.ok();
		}
		UserFavourite userFavourite = new UserFavourite();
		userFavourite.setType("article");
		userFavourite.setFavouriteId(articleId);
		userFavourite.setTitle(article.getTitle());
		userFavourite.setCreateTime(new Date());
		userFavourite.setUserId(myUser.getUserId());
		userFavouriteService.save(userFavourite);
		return ResponseBo.ok();
	}

	/**
	 * 新增收藏商品
	 * 
	 * @param productId 商品ID
	 */
	@RequestMapping("favourites/addProductFavourite")
	@ResponseBody
	public ResponseBo addProductFavourite(Long productId) {
		Product product = productService.selectByKey(productId);
		if (null == product) {
			ResponseBo.error();
		}
		MyUser myUser = this.getCurrentUser();
		// 判断是否已经收藏
		Example favouriteExample = new Example(UserFavourite.class);
		favouriteExample.createCriteria().andEqualTo("userId", myUser.getUserId()).andEqualTo("type", "product")
				.andEqualTo("favouriteId", productId);
		List<UserFavourite> userFavourites = userFavouriteService.selectByExample(favouriteExample);
		if (null != userFavourites && userFavourites.size() > 0) {
			return ResponseBo.ok();
		}
		UserFavourite userFavourite = new UserFavourite();
		userFavourite.setType("product");
		userFavourite.setFavouriteId(productId);
		userFavourite.setTitle(product.getTitle());
		userFavourite.setCreateTime(new Date());
		userFavourite.setUserId(myUser.getUserId());
		userFavouriteService.save(userFavourite);
		return ResponseBo.ok();
	}

	/**
	 * 取消收藏
	 * 
	 * @param type article/product
	 */
	@RequestMapping("favourites/cancelFavourite")
	@ResponseBody
	public ResponseBo cancelFavorite(String type, Long favoriteId) {
		Example favouriteExample = new Example(UserFavourite.class);
		favouriteExample.createCriteria().andEqualTo("type", type).andEqualTo("favouriteId", favoriteId);
		userFavouriteService.deleteByExample(favouriteExample);
		return ResponseBo.ok();
	}

	@RequestMapping("/favourites/delete")
	@ResponseBody
	public ResponseBo deleteCollects(String ids) {
		try {
			List<String> list = Arrays.asList(ids.split(","));
			this.userFavouriteService.batchDelete(list, "id", UserFavourite.class);
			return ResponseBo.ok("删除收藏成功！");
		} catch (Exception e) {
			log.error("删除收藏失败", e);
			return ResponseBo.error("删除收藏失败，请联系网站管理员！");
		}
	}

	/**
	 * 进入文章评论页
	 */
	@RequestMapping("/articleComment")
	public String articleCommentIndex() {
		return "member/userArticleComment";
	}

	@RequestMapping("/articleComment/save")
	@ResponseBody
	public ResponseBo addArticleComment(ArticleComment articleComment) {
		MyUser myUser = this.getCurrentUser();
		articleComment.setUserId(myUser.getUserId());
		articleComment.setCreateTime(new Date());
		articleComment.setAuthor(myUser.getUsername());
		Example example=new Example(Article.class);
		example.selectProperties("title").createCriteria().andEqualTo("id",articleComment.getArticleId());
		Article article = articleService.selectOneByExample(example);
		articleComment.setTitle(article.getTitle());
		// 默认为待审核
		articleComment.setStatus(ArticleComment.STATUS_LOCK);
		return ResponseBo.ok();
	}

	@RequestMapping("/articleComment/list")
	@ResponseBody
	public Map<String, Object> articleCommentList(QueryRequest request) {
		MyUser myUser = this.getCurrentUser();
		Example commentExample = new Example(ArticleComment.class);
		commentExample.createCriteria().andEqualTo("userId", myUser.getUserId());
		return super.selectByPageNumSize(request, () -> articleCommentService.selectByExample(commentExample));
	}

	@RequestMapping("/articleComment/delete")
	@ResponseBody
	public ResponseBo deleteArticleComments(String ids) {
		try {
			this.articleCommentService.deleteArticleComments(ids);
			return ResponseBo.ok("删除文章评论成功！");
		} catch (Exception e) {
			log.error("删除文章评论失败", e);
			return ResponseBo.error("删除文章评论失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/productComment")
	public String productCommentindex() {
		return "member/userProductComment";
	}

	@RequestMapping("/productComment/save")
	@ResponseBody
	public ResponseBo saveProductComment(ProductComment productComment) {
		MyUser myUser = this.getCurrentUser();
		productComment.setUserId(myUser.getUserId());
		productComment.setCreateTime(new Date());
		productComment.setAuthor(myUser.getUsername());
		Example example =new Example(Product.class);
		example.selectProperties("title").createCriteria().andEqualTo("id",productComment.getProductId());
		Product product = productService.selectOneByExample(example);
		productComment.setTitle(product.getTitle());
		// 状态为待审核
		productComment.setStatus(ProductComment.STATUS_LOCK);
		productCommentService.save(productComment);
		return ResponseBo.ok();
	}

	@RequestMapping("/productComment/list")
	@ResponseBody
	public Map<String, Object> productCommentList(QueryRequest request) {
		MyUser myUser = this.getCurrentUser();
		Example commentExample = new Example(ProductComment.class);
		commentExample.createCriteria().andEqualTo("userId", myUser.getUserId());
		return super.selectByPageNumSize(request, () -> productCommentService.selectByExample(commentExample));
	}

	@RequestMapping("/productComment/delete")
	@ResponseBody
	public ResponseBo deleteProductComments(String ids) {
		try {
			this.productCommentService.deleteProductComments(ids);
			return ResponseBo.ok("删除商品评论成功！");
		} catch (Exception e) {
			log.error("删除商品评论失败", e);
			return ResponseBo.error("删除商品评论失败，请联系网站管理员！");
		}
	}

	/**
	 * 分类tree形结构输出
	 */
	@RequestMapping("/category/tree")
	@ResponseBody
	public ResponseBo getCategoryTree() {
		try {
			Tree<ArticleCategory> tree = this.categoryService.getCategoryTree(null);
			return ResponseBo.ok(tree);
		} catch (Exception e) {
			log.error("获取分类树失败", e);
			return ResponseBo.error("获取分类树失败！");
		}
	}

	@RequestMapping("/userArticle")
	public String userArticle() {
		return "member/article";
	}

	@PostMapping("/getUserArticleList")
	@ResponseBody
	public Map<String, Object> getUserArticleList(@RequestBody QueryRequest request) {
		MyUser myUser = this.getCurrentUser();
		Long userId = myUser.getUserId();
		return super.selectByPageNumSize(request, () -> articleService.findUserArticles(userId));
	}

	@RequestMapping("/profile")
	public String profileIndex(Model model) {
		MyUser user = super.getCurrentUser();
		user = this.userService.findUserProfile(user);
		String ssex = user.getSsex();
		if (MyUser.SEX_MALE.equals(ssex)) {
			user.setSsex("性别：男");
		} else if (MyUser.SEX_FEMALE.equals(ssex)) {
			user.setSsex("性别：女");
		} else {
			user.setSsex("性别：保密");
		}
		model.addAttribute("user", user);
		return "member/profile";
	}

	@RequestMapping("/getUserProfile")
	@ResponseBody
	public ResponseBo getUserProfile(Long userId) {
		try {
			MyUser user = new MyUser();
			user.setUserId(userId);
			return ResponseBo.ok(this.userService.findUserProfile(user));
		} catch (Exception e) {
			log.error("获取用户信息失败", e);
			return ResponseBo.error("获取用户信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/updateUserProfile")
	@ResponseBody
	public ResponseBo updateUserProfile(MyUser user) {
		try {
			this.userService.updateUserProfile(user);
			return ResponseBo.ok("更新个人信息成功！");
		} catch (Exception e) {
			log.error("更新用户信息失败", e);
			return ResponseBo.error("更新用户信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/changeAvatar")
	@ResponseBody
	public ResponseBo changeAvatar(String imgName) {
		try {
			String[] img = imgName.split("/");
			String realImgName = img[img.length - 1];
			MyUser user = getCurrentUser();
			user.setAvatar(realImgName);
			this.userService.updateNotNull(user);
			return ResponseBo.ok("更新头像成功！");
		} catch (Exception e) {
			log.error("更换头像失败", e);
			return ResponseBo.error("更新头像失败，请联系网站管理员！");
		}
	}

	@GetMapping("/addArticle")
	public String addArticle() {
		return "member/articleAdd";
	}

	@GetMapping("/editArticle")
	public String editArticle(HttpServletRequest request, Long articleId) {
		request.setAttribute("articleId", articleId);
		return "member/articleAdd";
	}

	/**
	 * 会员文章投递
	 */
	@RequestMapping("/saveArticle")
	@ResponseBody
	public ResponseBo saveArticle(Article article, HttpServletRequest request) {
		try {
			MyUser currentUser = this.getCurrentUser();
			article.setAuthor(currentUser.getUsername());
			article.setUserId(currentUser.getUserId());
			article.setStatus(Article.STATUS_LOCK);
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

	@RequestMapping("/updateArticle")
	@ResponseBody
	public ResponseBo updateArticle(Article article, HttpServletRequest request) {
		try {
			article.setStatus(Article.STATUS_LOCK);
			this.articleService.updateArticle(article);
			Long articleId = article.getId();
			// 分类相关处理
			this.articleCategoryService.deleteArticleCategoryMapping(articleId);
			String catIds = article.getCategoryIds();
			String[] categoryIds = catIds.split(",");
			for (String catId : categoryIds) {
				Long categoryId = Long.parseLong(catId);
				ArticleCategoryMapping articleCategoryMapping = new ArticleCategoryMapping();
				articleCategoryMapping.setArticleId(articleId);
				articleCategoryMapping.setCategoryId(categoryId);
				articleCategoryService.save(articleCategoryMapping);
			}
			// 先删除原有引用关系，再重新创建
			List<ArticleTag> _tags = articleTagService.getTagByArticleIds(articleId.toString());
			for (ArticleTag articleTag : _tags) {
				if (null != articleTag) {
					int counts = articleTag.getCounts();
					articleTag.setCounts(counts - 1);
					articleTagService.updateArticleTag(articleTag);
				}
			}
			this.articleTagMappingService.deleteArticleTagMapping(articleId);

			String tagIds = request.getParameter("tags");
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
}

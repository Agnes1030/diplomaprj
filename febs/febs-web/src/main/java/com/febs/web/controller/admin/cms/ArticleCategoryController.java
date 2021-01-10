package com.febs.web.controller.admin.cms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.ArticleCategory;
import com.febs.cms.domain.NavMenu;
import com.febs.cms.service.ArticleCategoryMappingService;
import com.febs.cms.service.ArticleCategoryService;
import com.febs.cms.service.NavMenuService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.ResponseBo;
import com.febs.common.domain.Tree;
import com.febs.common.utils.FileUtils;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class ArticleCategoryController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ArticleCategoryService categoryService;
	@Autowired
	private NavMenuService navMenuService;
	@Autowired
	private ArticleCategoryMappingService articleCategoryMappingService;

	@Log("获取分类信息")
	@RequestMapping("/category")
	@PreAuthorize("hasAuthority('category:list')")
	public String index() {
		return "admin/cms/category/category";
	}

	@RequestMapping("/category/tree")
	@PreAuthorize("hasAuthority('category:list')")
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

	@RequestMapping("/category/getCategory")
	@ResponseBody
	public ResponseBo getCategory(Long categoryId) {
		try {
			ArticleCategory Category = this.categoryService.findById(categoryId);
			return ResponseBo.ok(Category);
		} catch (Exception e) {
			log.error("获取分类信息失败", e);
			return ResponseBo.error("获取分类信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/category/list")
	@PreAuthorize("hasAuthority('category:list')")
	@ResponseBody
	public List<ArticleCategory> categoryList(ArticleCategory category) {
		try {
			return this.categoryService.findAllCategorys(category);
		} catch (Exception e) {
			log.error("获取分类列表失败", e);
			return new ArrayList<>();
		}
	}

	@RequestMapping("/category/checkCategoryName")
	@ResponseBody
	public boolean checkCategoryName(String categoryName, String oldCategoryName) {
		if (StringUtils.isNotBlank(oldCategoryName) && StringUtils.equalsIgnoreCase(categoryName, oldCategoryName)) {
			return true;
		}
		ArticleCategory result = this.categoryService.findByName(categoryName);
		return result == null;
	}

	@Log("新增分类 ")
	@PreAuthorize("hasAuthority('category:add')")
	@RequestMapping("/category/add")
	@ResponseBody
	public ResponseBo addCategory(ArticleCategory category) {
		try {
			MyUser currentUser = this.getCurrentUser();
			category.setUserId(currentUser.getUserId());
			this.categoryService.addCategory(category);
			if (category.getNavShow().equals(ArticleCategory.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(category.getName());
				navMenu.setSeqNum(category.getSeqNum());
				navMenu.setNavType("category");
				navMenu.setRelationId(category.getId());
				navMenu.setNavUrl(category.getLinkUrl());
				navMenu.setTarget("_self");
				if (null != category.getParentId() && category.getParentId() > 0) {
					NavMenu parentMenu = new NavMenu();
					parentMenu.setNavType("category");
					parentMenu.setRelationId(category.getParentId());
					List<NavMenu> menus = navMenuService.findAllNavMenus(parentMenu);
					if (null != menus && menus.size() > 0) {
						navMenu.setParentId(menus.get(0).getId());
					}
				} else {
					navMenu.setParentId(0L);
				}
				navMenuService.addNavMenu(navMenu);
			}
			return ResponseBo.ok("新增分类成功！");
		} catch (Exception e) {
			log.error("新增分类失败", e);
			return ResponseBo.error("新增分类失败，请联系网站管理员！");
		}
	}

	@Log("删除分类")
	@PreAuthorize("hasAuthority('category:delete')")
	@RequestMapping("/category/delete")
	@ResponseBody
	public ResponseBo deleteCategorys(String ids) {
		try {
			this.categoryService.deleteCategorys(ids);
			this.articleCategoryMappingService.deleteCategoryMappingByCat(ids);
			this.navMenuService.deleteNavByCategoryIds(ids);
			return ResponseBo.ok("删除分类成功！");
		} catch (Exception e) {
			log.error("删除分类失败", e);
			return ResponseBo.error("删除分类失败，请联系网站管理员！");
		}
	}

	@Log("修改分类 ")
	@PreAuthorize("hasAuthority('category:update')")
	@RequestMapping("/category/update")
	@ResponseBody
	public ResponseBo updateCategory(ArticleCategory category) {
		try {
			MyUser currentUser = this.getCurrentUser();
			category.setUserId(currentUser.getUserId());
			this.categoryService.updateCategory(category);
			// 首页判断原来导航菜单中是否存在，如果已经存在直接把原来主键取到进行更新即可
			NavMenu params = new NavMenu();
			params.setNavType("category");
			params.setRelationId(category.getId());
			List<NavMenu> _menus = navMenuService.findAllNavMenus(params);
			if (category.getNavShow().equals(ArticleCategory.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(category.getName());
				navMenu.setSeqNum(category.getSeqNum());
				navMenu.setNavType("category");
				navMenu.setRelationId(category.getId());
				navMenu.setTarget("_self");
				navMenu.setNavUrl(category.getLinkUrl());
				navMenu.setUserId(currentUser.getUserId());
				if (null != category.getParentId() && category.getParentId() > 0) {
					NavMenu parentMenu = new NavMenu();
					parentMenu.setNavType("category");
					parentMenu.setRelationId(category.getParentId());
					List<NavMenu> menus = navMenuService.findAllNavMenus(parentMenu);
					if (null != menus && menus.size() > 0) {
						navMenu.setParentId(menus.get(0).getId());
					}
				} else {
					navMenu.setParentId(0L);
				}

				if (null != _menus && _menus.size() > 0) {
					navMenu.setId(_menus.get(0).getId());
					// 先删除原来的菜单再新添加
					navMenuService.updateNavMenu(navMenu);
				} else {
					navMenuService.addNavMenu(navMenu);
				}
			} else {
				if (null != _menus && _menus.size() > 0) {
					String menuId = _menus.get(0).getId().toString();
					navMenuService.deleteNavMenus(menuId);
				}
			}
			return ResponseBo.ok("修改分类成功！");
		} catch (Exception e) {
			log.error("修改分类失败", e);
			return ResponseBo.error("修改分类失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/category/excel")
	@ResponseBody
	public ResponseBo categoryExcel(ArticleCategory category) {
		try {
			List<ArticleCategory> list = this.categoryService.findAllCategorys(category);
			return FileUtils.createExcelByPOIKit("分类表", list, ArticleCategory.class);
		} catch (Exception e) {
			log.error("导出分类信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/category/csv")
	@ResponseBody
	public ResponseBo categoryCsv(ArticleCategory category) {
		try {
			List<ArticleCategory> list = this.categoryService.findAllCategorys(category);
			return FileUtils.createCsv("分类表", list, ArticleCategory.class);
		} catch (Exception e) {
			log.error("获取分类信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

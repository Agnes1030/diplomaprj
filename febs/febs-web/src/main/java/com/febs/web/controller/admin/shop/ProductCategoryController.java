package com.febs.web.controller.admin.shop;

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

import com.febs.cms.domain.NavMenu;
import com.febs.cms.service.NavMenuService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.ResponseBo;
import com.febs.common.domain.Tree;
import com.febs.common.utils.FileUtils;
import com.febs.shop.domain.ProductCategory;
import com.febs.shop.service.ProductCategoryService;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class ProductCategoryController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private NavMenuService navMenuService;

	@Log("获取分类信息")
	@RequestMapping("/productCategory")
	@PreAuthorize("hasAuthority('productCategory:list')")
	public String index() {
		return "admin/shop/productCategory/productCategory";
	}

	@RequestMapping("/productCategory/tree")
	@ResponseBody
	public ResponseBo getProductCategoryTree() {
		try {
			Tree<ProductCategory> tree = this.productCategoryService.getProductCategoryTree(null);
			return ResponseBo.ok(tree);
		} catch (Exception e) {
			log.error("获取分类树失败", e);
			return ResponseBo.error("获取分类树失败！");
		}
	}

	@RequestMapping("/productCategory/getProductCategory")
	@ResponseBody
	public ResponseBo getProductCategory(Long productCategoryId) {
		try {
			ProductCategory productCategory = this.productCategoryService.findById(productCategoryId);
			return ResponseBo.ok(productCategory);
		} catch (Exception e) {
			log.error("获取分类信息失败", e);
			return ResponseBo.error("获取分类信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/productCategory/list")
	@PreAuthorize("hasAuthority('productCategory:list')")
	@ResponseBody
	public List<ProductCategory> productCategoryList(ProductCategory productCategory) {
		try {
			return this.productCategoryService.findAllProductCategorys(productCategory);
		} catch (Exception e) {
			log.error("获取分类列表失败", e);
			return new ArrayList<>();
		}
	}

	@RequestMapping("/productCategory/checkProductCategoryName")
	@ResponseBody
	public boolean checkProductCategoryName(String productCategoryName, String oldProductCategoryName) {
		if (StringUtils.isNotBlank(oldProductCategoryName)
				&& StringUtils.equalsIgnoreCase(productCategoryName, oldProductCategoryName)) {
			return true;
		}
		ProductCategory result = this.productCategoryService.findByName(productCategoryName);
		return result == null;
	}

	@Log("新增分类 ")
	@PreAuthorize("hasAuthority('productCategory:add')")
	@RequestMapping("/productCategory/add")
	@ResponseBody
	public ResponseBo addProductCategory(ProductCategory productCategory) {
		try {
			MyUser currentUser = this.getCurrentUser();
			productCategory.setUserId(currentUser.getUserId());
			this.productCategoryService.addProductCategory(productCategory);
			if (productCategory.getNavShow().equals(ProductCategory.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(productCategory.getName());
				navMenu.setSeqNum(productCategory.getSeqNum());
				navMenu.setNavType("productCategory");
				navMenu.setRelationId(productCategory.getId());
				navMenu.setNavUrl("/productCategory/" + productCategory.getSlug());
				navMenu.setTarget("_self");
				if (null != productCategory.getParentId() && productCategory.getParentId() > 0) {
					NavMenu parentMenu = new NavMenu();
					parentMenu.setNavType("productCategory");
					parentMenu.setRelationId(productCategory.getParentId());
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
	@PreAuthorize("hasAuthority('productCategory:delete')")
	@RequestMapping("/productCategory/delete")
	@ResponseBody
	public ResponseBo deleteProductCategorys(String ids) {
		try {
			this.productCategoryService.deleteProductCategorys(ids);
			this.navMenuService.deleteNavByProductCategoryIds(ids);
			return ResponseBo.ok("删除分类成功！");
		} catch (Exception e) {
			log.error("删除分类失败", e);
			return ResponseBo.error("删除分类失败，请联系网站管理员！");
		}
	}

	@Log("修改分类 ")
	@PreAuthorize("hasAuthority('productCategory:update')")
	@RequestMapping("/productCategory/update")
	@ResponseBody
	public ResponseBo updateProductCategory(ProductCategory productCategory) {
		try {
			MyUser currentUser = this.getCurrentUser();
			productCategory.setUserId(currentUser.getUserId());
			this.productCategoryService.updateProductCategory(productCategory);
			// 首页判断原来导航菜单中是否存在，如果已经存在直接把原来主键取到进行更新即可
			NavMenu params = new NavMenu();
			params.setNavType("productCategory");
			params.setRelationId(productCategory.getId());
			List<NavMenu> _menus = navMenuService.findAllNavMenus(params);
			if (productCategory.getNavShow().equals(ProductCategory.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(productCategory.getName());
				navMenu.setSeqNum(productCategory.getSeqNum());
				navMenu.setNavType("productCategory");
				navMenu.setRelationId(productCategory.getId());
				navMenu.setTarget("_self");
				navMenu.setNavUrl("/productCategory/" + productCategory.getSlug());
				navMenu.setUserId(currentUser.getUserId());
				if (null != productCategory.getParentId() && productCategory.getParentId() > 0) {
					NavMenu parentMenu = new NavMenu();
					parentMenu.setNavType("productCategory");
					parentMenu.setRelationId(productCategory.getParentId());
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

	@RequestMapping("/productCategory/excel")
	@ResponseBody
	public ResponseBo productCategoryExcel(ProductCategory productCategory) {
		try {
			List<ProductCategory> list = this.productCategoryService.findAllProductCategorys(productCategory);
			return FileUtils.createExcelByPOIKit("分类表", list, ProductCategory.class);
		} catch (Exception e) {
			log.error("导出分类信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/productCategory/csv")
	@ResponseBody
	public ResponseBo productCategoryCsv(ProductCategory productCategory) {
		try {
			List<ProductCategory> list = this.productCategoryService.findAllProductCategorys(productCategory);
			return FileUtils.createCsv("分类表", list, ProductCategory.class);
		} catch (Exception e) {
			log.error("获取分类信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

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

import com.febs.cms.domain.NavMenu;
import com.febs.cms.service.NavMenuService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.ResponseBo;
import com.febs.common.domain.Tree;
import com.febs.common.utils.FileUtils;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;
/**
 * 前台导航菜单controller
 * @author wtsoftware 
 * @date 2020-06-14
 */
@Controller
@RequestMapping("/admin")
public class NavMenuController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private NavMenuService navMenuService;

	@Log("获取导航信息")
	@RequestMapping("/navMenu")
	@PreAuthorize("hasAuthority('navMenu:list')")
	public String index() {
		return "admin/cms/navMenu/navMenu";
	}

	@RequestMapping("/navMenu/tree")
	@ResponseBody
	public ResponseBo getNavMenuTree() {
		try {
			Tree<NavMenu> tree = this.navMenuService.getNavMenuTree();
			return ResponseBo.ok(tree);
		} catch (Exception e) {
			log.error("获取导航树失败", e);
			return ResponseBo.error("获取导航树失败！");
		}
	}

	@RequestMapping("/navMenu/getNavMenu")
	@ResponseBody
	public ResponseBo getNavMenu(Long navMenuId) {
		try {
			NavMenu NavMenu = this.navMenuService.findById(navMenuId);
			return ResponseBo.ok(NavMenu);
		} catch (Exception e) {
			log.error("获取导航信息失败", e);
			return ResponseBo.error("获取导航信息失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/navMenu/list")
	@PreAuthorize("hasAuthority('navMenu:list')")
	@ResponseBody
	public List<NavMenu> navMenuList(NavMenu navMenu) {
		try {
			return this.navMenuService.findAllNavMenus(navMenu);
		} catch (Exception e) {
			log.error("获取导航列表失败", e);
			return new ArrayList<>();
		}
	}

	@RequestMapping("/navMenu/checkNavMenuName")
	@ResponseBody
	public boolean checkNavMenuName(String navMenuName, String oldNavMenuName) {
		if (StringUtils.isNotBlank(oldNavMenuName) && StringUtils.equalsIgnoreCase(navMenuName, oldNavMenuName)) {
			return true;
		}
		NavMenu result = this.navMenuService.findByName(navMenuName);
		return result == null;
	}

	@Log("新增导航 ")
	@PreAuthorize("hasAuthority('navMenu:add')")
	@RequestMapping("/navMenu/add")
	@ResponseBody
	public ResponseBo addNavMenu(NavMenu navMenu) {
		try {
			MyUser currentUser = this.getCurrentUser();
			navMenu.setUserId(currentUser.getUserId());
			this.navMenuService.addNavMenu(navMenu);
			return ResponseBo.ok("新增导航成功！");
		} catch (Exception e) {
			log.error("新增导航失败", e);
			return ResponseBo.error("新增导航失败，请联系网站管理员！");
		}
	}

	@Log("删除导航")
	@PreAuthorize("hasAuthority('navMenu:delete')")
	@RequestMapping("/navMenu/delete")
	@ResponseBody
	public ResponseBo deleteNavMenus(String ids) {
		try {
			this.navMenuService.deleteNavMenus(ids);
			return ResponseBo.ok("删除导航成功！");
		} catch (Exception e) {
			log.error("删除导航失败", e);
			return ResponseBo.error("删除导航失败，请联系网站管理员！");
		}
	}

	@Log("修改导航 ")
	@PreAuthorize("hasAuthority('navMenu:update')")
	@RequestMapping("/navMenu/update")
	@ResponseBody
	public ResponseBo updateNavMenu(NavMenu navMenu) {
		try {
			MyUser currentUser = this.getCurrentUser();
			navMenu.setUserId(currentUser.getUserId());
			this.navMenuService.updateNavMenu(navMenu);
			return ResponseBo.ok("修改导航成功！");
		} catch (Exception e) {
			log.error("修改导航失败", e);
			return ResponseBo.error("修改导航失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/navMenu/excel")
	@ResponseBody
	public ResponseBo navMenuExcel(NavMenu navMenu) {
		try {
			List<NavMenu> list = this.navMenuService.findAllNavMenus(navMenu);
			return FileUtils.createExcelByPOIKit("导航表", list, NavMenu.class);
		} catch (Exception e) {
			log.error("导出导航信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/navMenu/csv")
	@ResponseBody
	public ResponseBo navMenuCsv(NavMenu navMenu) {
		try {
			List<NavMenu> list = this.navMenuService.findAllNavMenus(navMenu);
			return FileUtils.createCsv("导航表", list, NavMenu.class);
		} catch (Exception e) {
			log.error("获取导航信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

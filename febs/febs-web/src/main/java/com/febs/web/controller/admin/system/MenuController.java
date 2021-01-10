package com.febs.web.controller.admin.system;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.common.annotation.Log;
import com.febs.common.domain.ResponseBo;
import com.febs.common.domain.Tree;
import com.febs.common.utils.FileUtils;
import com.febs.security.domain.FebsSocialUserDetails;
import com.febs.security.domain.FebsUserDetails;
import com.febs.system.domain.Menu;
import com.febs.system.domain.MyUser;
import com.febs.system.service.MenuService;
import com.febs.web.controller.base.BaseController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin")
public class MenuController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuService menuService;
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;
    @RequestMapping("/menu")
    @PreAuthorize("hasAuthority('menu:list')")
    public String index() {
        return "admin/system/menu/menu";
    }

    @RequestMapping("/menu/menu")
    @PreAuthorize("hasAuthority('menu:list')")
    @ResponseBody
    public ResponseBo getMenu(String userName) {
        try {
            List<Menu> menus = this.menuService.findUserMenus(userName);
            return ResponseBo.ok(menus);
        } catch (Exception e) {
            logger.error("获取菜单失败", e);
            return ResponseBo.error("获取菜单失败！");
        }
    }

    @RequestMapping("/menu/getMenu")
    @ResponseBody
    public ResponseBo getMenu(Long menuId) {
        try {
            Menu menu = this.menuService.findById(menuId);
            return ResponseBo.ok(menu);
        } catch (Exception e) {
            logger.error("获取菜单信息失败", e);
            return ResponseBo.error("获取信息失败，请联系网站管理员！");
        }
    }

    @RequestMapping("/menu/menuButtonTree")
    @ResponseBody
    public ResponseBo getMenuButtonTree() {
        try {
            Tree<Menu> tree = this.menuService.getMenuButtonTree();
            return ResponseBo.ok(tree);
        } catch (Exception e) {
            logger.error("获取菜单列表失败", e);
            return ResponseBo.error("获取菜单列表失败！");
        }
    }

    @RequestMapping("/menu/tree")
    @ResponseBody
    public ResponseBo getMenuTree() {
        try {
            Tree<Menu> tree = this.menuService.getMenuTree();
            return ResponseBo.ok(tree);
        } catch (Exception e) {
            logger.error("获取菜单树失败", e);
            return ResponseBo.error("获取菜单树失败！");
        }
    }

    @RequestMapping("/menu/getUserMenu")
    @ResponseBody
    public ResponseBo getUserMenu(String userName) {
        try {
            Tree<Menu> tree = this.menuService.getUserMenu(userName);
            return ResponseBo.ok(tree);
        } catch (Exception e) {
            logger.error("获取用户菜单失败", e);
            return ResponseBo.error("获取用户菜单失败！");
        }
    }

    @RequestMapping("/menu/list")
    @ResponseBody
    public List<Menu> menuList(Menu menu) {
        try {
            return this.menuService.findAllMenus(menu);
        } catch (Exception e) {
            logger.error("获取菜单集合失败", e);
            return new ArrayList<>();
        }
    }


    @RequestMapping("/menu/checkMenuName")
    @ResponseBody
    public boolean checkMenuName(String menuName, String type, String oldMenuName) {
        if (StringUtils.isNotBlank(oldMenuName) && StringUtils.equalsIgnoreCase(menuName, oldMenuName)) {
            return true;
        }
        Menu result = this.menuService.findByNameAndType(menuName, type);
        return result == null;
    }

    @PreAuthorize("hasAuthority('menu:add')")
    @RequestMapping("/menu/add")
    @ResponseBody
    public ResponseBo addMenu(Menu menu) {
        String name;
        if (Menu.TYPE_MENU.equals(menu.getType())) {
            name = "菜单";
        } else {
            name = "按钮";
        }
        try {
            this.menuService.addMenu(menu);
            refreshAdminMenu();
            return ResponseBo.ok("新增" + name + "成功！");
        } catch (Exception e) {
            logger.error("新增{}失败", name, e);
            return ResponseBo.error("新增" + name + "失败，请联系网站管理员！");
        }
    }

    @PreAuthorize("hasAuthority('menu:delete')")
    @RequestMapping("/menu/delete")
    @ResponseBody
    public ResponseBo deleteMenus(String ids) {
        try {
            this.menuService.deleteMeuns(ids);
            refreshAdminMenu();
            return ResponseBo.ok("删除成功！");
        } catch (Exception e) {
            logger.error("获取菜单失败", e);
            return ResponseBo.error("删除失败，请联系网站管理员！");
        }
    }

    @PreAuthorize("hasAuthority('menu:update')")
    @RequestMapping("/menu/update")
    @ResponseBody
    public ResponseBo updateMenu(Menu menu) {
        String name;
        if (Menu.TYPE_MENU.equals(menu.getType()))
            name = "菜单";
        else
            name = "按钮";
        try {
            this.menuService.updateMenu(menu);
            refreshAdminMenu();
            return ResponseBo.ok("修改" + name + "成功！");
        } catch (Exception e) {
            logger.error("修改{}失败", name, e);
            return ResponseBo.error("修改" + name + "失败，请联系网站管理员！");
        }
    }

    @Log("获取系统所有URL")
    @GetMapping("/menu/urlList")
    @ResponseBody
    public List<Map<String, String>> getAllUrl() {
        return this.menuService.getAllUrl("1");
    }

    @RequestMapping("/menu/excel")
    @ResponseBody
    public ResponseBo menuExcel(Menu menu) {
        try {
            List<Menu> list = this.menuService.findAllMenus(menu);
            return FileUtils.createExcelByPOIKit("菜单表", list, Menu.class);
        } catch (Exception e) {
            logger.error("带出菜单列表Excel失败", e);
            return ResponseBo.error("导出Excel失败，请联系网站管理员！");
        }
    }

    @RequestMapping("/menu/csv")
    @ResponseBody
    public ResponseBo menuCsv(Menu menu) {
        try {
            List<Menu> list = this.menuService.findAllMenus(menu);
            return FileUtils.createCsv("菜单表", list, Menu.class);
        } catch (Exception e) {
            logger.error("导出菜单列表Csv失败", e);
            return ResponseBo.error("导出Csv失败，请联系网站管理员！");
        }
    }
    public void refreshAdminMenu() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		// 后台管理菜单列表
		Map<String, Object> vars = new HashMap<>(1);
		MyUser user = new MyUser();
		if (principal instanceof FebsUserDetails) {
			FebsUserDetails userDetails = (FebsUserDetails) principal;
			user.setUserId(userDetails.getUserId());
			user.setPassword(userDetails.getPassword());
			user.setUsername(userDetails.getUsername());
		} else {
			FebsSocialUserDetails userDetails = (FebsSocialUserDetails) principal;
			user.setUserId(userDetails.getUsersId());
			user.setPassword(userDetails.getPassword());
			user.setUsername(userDetails.getUsername());
		}
		String userName = user.getUsername();
		Tree<Menu> treeMenu = menuService.getUserMenu(userName);
		vars.put("treeMenu", treeMenu.getChildren());
		thymeleafViewResolver.setStaticVariables(vars);
    }
}

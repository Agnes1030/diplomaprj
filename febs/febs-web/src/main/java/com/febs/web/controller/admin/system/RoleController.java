package com.febs.web.controller.admin.system;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.domain.Tree;
import com.febs.common.utils.FileUtils;
import com.febs.security.domain.FebsSocialUserDetails;
import com.febs.security.domain.FebsUserDetails;
import com.febs.system.domain.Menu;
import com.febs.system.domain.MyUser;
import com.febs.system.domain.Role;
import com.febs.system.service.MenuService;
import com.febs.system.service.RoleService;
import com.febs.web.controller.base.BaseController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin")
public class RoleController extends BaseController {

    private Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @Log("获取角色信息")
    @RequestMapping("/role")
    @PreAuthorize("hasAuthority('role:list')")
    public String index() {
        return "admin/system/role/role";
    }

    @RequestMapping("/role/list")
    @PreAuthorize("hasAuthority('role:list')")
    @ResponseBody
    public Map<String, Object> roleList(QueryRequest request, Role role) {
        return super.selectByPageNumSize(request, () -> this.roleService.findAllRole(role));
    }

    @RequestMapping("/role/getRole")
    @ResponseBody
    public ResponseBo getRole(Long roleId) {
        try {
            Role role = this.roleService.findRoleWithMenus(roleId);
            return ResponseBo.ok(role);
        } catch (Exception e) {
            log.error("获取角色信息失败", e);
            return ResponseBo.error("获取角色信息失败，请联系网站管理员！");
        }
    }

    @RequestMapping("/role/checkRoleName")
    @ResponseBody
    public boolean checkRoleName(String roleName, String oldRoleName) {
        if (StringUtils.isNotBlank(oldRoleName) && StringUtils.equalsIgnoreCase(roleName, oldRoleName)) {
            return true;
        }
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    @Log("新增角色")
    @PreAuthorize("hasAuthority('role:add')")
    @RequestMapping("/role/add")
    @ResponseBody
    public ResponseBo addRole(Role role, Long[] menuId) {
        try {
            this.roleService.addRole(role, menuId);
            refreshAdminMenu();
            return ResponseBo.ok("新增角色成功！");
        } catch (Exception e) {
            log.error("新增角色失败", e);
            return ResponseBo.error("新增角色失败，请联系网站管理员！");
        }
    }

    @Log("删除角色")
    @PreAuthorize("hasAuthority('role:delete')")
    @RequestMapping("/role/delete")
    @ResponseBody
    public ResponseBo deleteRoles(String ids) {
        try {
            this.roleService.deleteRoles(ids);
            refreshAdminMenu();
            return ResponseBo.ok("删除角色成功！");
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return ResponseBo.error("删除角色失败，请联系网站管理员！");
        }
    }

    @Log("修改角色")
    @PreAuthorize("hasAuthority('role:update')")
    @RequestMapping("/role/update")
    @ResponseBody
    public ResponseBo updateRole(Role role, Long[] menuId) {
        try {
            this.roleService.updateRole(role, menuId);
            refreshAdminMenu();
            return ResponseBo.ok("修改角色成功！");
        } catch (Exception e) {
            log.error("修改角色失败", e);
            return ResponseBo.error("修改角色失败，请联系网站管理员！");
        }
    }

    @RequestMapping("/role/excel")
    @ResponseBody
    public ResponseBo roleExcel(Role role) {
        try {
            List<Role> list = this.roleService.findAllRole(role);
            return FileUtils.createExcelByPOIKit("角色表", list, Role.class);
        } catch (Exception e) {
            log.error("导出角色信息Excel失败", e);
            return ResponseBo.error("导出Excel失败，请联系网站管理员！");
        }
    }

    @RequestMapping("/role/csv")
    @ResponseBody
    public ResponseBo roleCsv(Role role) {
        try {
            List<Role> list = this.roleService.findAllRole(role);
            return FileUtils.createCsv("角色表", list, Role.class);
        } catch (Exception e) {
            log.error("导出角色信息Csv失败", e);
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

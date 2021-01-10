package com.febs.web.controller.admin.cms;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.NavMenu;
import com.febs.cms.domain.SinglePage;
import com.febs.cms.service.NavMenuService;
import com.febs.cms.service.SinglePageService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;

/**
 * 单页管理controller
 * 
 * @author wtsoftware
 * @date 2020-06-14
 */
@Controller
@RequestMapping("/admin")
public class SinglePageController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SinglePageService singlePageService;
	@Autowired
	private NavMenuService navMenuService;

	@Log("单页列表")
	@RequestMapping("/singlePage")
	@PreAuthorize("hasAuthority('singlePage:list')")
	public String index() {
		return "admin/cms/singlePage/singlePage";
	}

	@Log("获取单页列表信息")
	@RequestMapping("/singlePage/list")
	@PreAuthorize("hasAuthority('singlePage:list')")
	@ResponseBody
	public Map<String, Object> singlePageList(QueryRequest request, SinglePage singlePage) {
		log.debug("查询单页列表");
		return super.selectByPageNumSize(request, () -> this.singlePageService.findAllSinglePages(singlePage));
	}

	@RequestMapping("/singlePage/getSinglePage")
	@ResponseBody
	public ResponseBo getSinglePage(Long singlePageId) {
		try {
			SinglePage singlePage = this.singlePageService.findById(singlePageId);
			return ResponseBo.ok(singlePage);
		} catch (Exception e) {
			log.error("获取分类信息失败", e);
			return ResponseBo.error("获取分类信息失败，请联系网站管理员！");
		}
	}

	@GetMapping("/singlePage/add")
	public String toAddSinglePage(HttpServletRequest request) {
		request.setAttribute("action", "save");
		return "/admin/cms/singlePage/singlePageAdd";
	}

	@Log("新增单页 ")
	@PreAuthorize("hasAuthority('singlePage:add')")
	@PostMapping("/singlePage/add")
	@ResponseBody
	public ResponseBo addSinglePage(SinglePage singlePage) {
		try {
			MyUser currentUser = this.getCurrentUser();
			singlePage.setAuthor(currentUser.getUsername());
			this.singlePageService.addSinglePage(singlePage);
			if (singlePage.getNavShow().equals(SinglePage.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(singlePage.getTitle());
				navMenu.setSeqNum(singlePage.getSeqNum());
				navMenu.setNavType("page");
				navMenu.setRelationId(singlePage.getId());
				navMenu.setNavUrl("/page/" + singlePage.getSlug());
				navMenu.setTarget("_self");
				navMenu.setStatus(NavMenu.STATUS_VALID);
				// 后期完善以后可以直接在单页选低级导航，目前在单页暂时只支持增加一级导航。如果要调整，可以在导航栏目调整栏目结构
				navMenu.setParentId(0L);
				navMenuService.addNavMenu(navMenu);
			}
			return ResponseBo.ok("新增单页成功！");
		} catch (Exception e) {
			log.error("新增单页失败", e);
			return ResponseBo.error("新增单页失败，请联系网站管理员！");
		}
	}

	@GetMapping("/singlePage/update")
	public String toUpdateSinglePage(HttpServletRequest request,Integer singlePageId) {
		request.setAttribute("singlePageId", singlePageId);
		request.setAttribute("action", "update");
		return "/admin/cms/singlePage/singlePageAdd";
	}
	@Log("修改单页 ")
	@PreAuthorize("hasAuthority('singlePage:update')")
	@PostMapping("/singlePage/update")
	@ResponseBody
	public ResponseBo updateSinglePage(SinglePage singlePage) {
		try {
			this.singlePageService.updateSinglePage(singlePage);
			if (singlePage.getNavShow().equals(SinglePage.STATUS_VALID)) {
				NavMenu navMenu = new NavMenu();
				navMenu.setName(singlePage.getTitle());
				navMenu.setSeqNum(singlePage.getSeqNum());
				navMenu.setNavType("page");
				navMenu.setRelationId(singlePage.getId());
				navMenu.setTarget("_self");
				navMenu.setNavUrl("/page/" + singlePage.getSlug());
				navMenu.setParentId(0L);
				navMenu.setStatus(NavMenu.STATUS_VALID);
				// 先删除原来的菜单再新添加
				navMenuService.deleteNav(navMenu);
				navMenuService.addNavMenu(navMenu);
			} else {
				NavMenu navMenu = new NavMenu();
				navMenu.setNavType("page");
				navMenu.setRelationId(singlePage.getId());
				navMenuService.deleteNav(navMenu);
			}
			return ResponseBo.ok("修改单页成功！");
		} catch (Exception e) {
			log.error("修改单页失败", e);
			return ResponseBo.error("修改单页失败，请联系网站管理员！");
		}
	}

	@Log("删除单页")
	@PreAuthorize("hasAuthority('singlePage:delete')")
	@RequestMapping("/singlePage/delete")
	@ResponseBody
	public ResponseBo deleteSinglePages(String ids) {
		try {
			this.singlePageService.deleteSinglePages(ids);
			List<String> list = Arrays.asList(ids.split(","));
			for (String id : list) {
				NavMenu navMenu = new NavMenu();
				navMenu.setNavType("page");
				navMenu.setRelationId(Long.parseLong(id));
				navMenuService.deleteNav(navMenu);
			}
			return ResponseBo.ok("删除单页成功！");
		} catch (Exception e) {
			log.error("删除单页失败", e);
			return ResponseBo.error("删除单页失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/singlePage/excel")
	@ResponseBody
	public ResponseBo deptExcel(SinglePage singlePage) {
		try {
			List<SinglePage> list = this.singlePageService.findAllSinglePages(singlePage);
			return FileUtils.createExcelByPOIKit("单页表", list, SinglePage.class);
		} catch (Exception e) {
			log.error("导出单页信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/singlePage/csv")
	@ResponseBody
	public ResponseBo deptCsv(SinglePage singlePage) {
		try {
			List<SinglePage> list = this.singlePageService.findAllSinglePages(singlePage);
			return FileUtils.createCsv("单页表", list, SinglePage.class);
		} catch (Exception e) {
			log.error("获取单页信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

package com.febs.web.controller.admin.cms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.Banner;
import com.febs.cms.service.BannerService;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class BannerController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BannerService bannerService;

	@RequestMapping("/banner")
	@PreAuthorize("hasAuthority('banner:list')")
	public String index() {
		return "admin/cms/banner/banner";
	}

	@RequestMapping("/banner/list")
	@PreAuthorize("hasAuthority('banner:list')")
	@ResponseBody
	public List<Banner> bannerList(QueryRequest request) {
		return this.bannerService.getAllBanners();
	}

	@RequestMapping("/banner/getBanner")
	@ResponseBody
	public ResponseBo getBanner(Long bannerId) {
		try {
			Banner banner = this.bannerService.findById(bannerId);
			return ResponseBo.ok(banner);
		} catch (Exception e) {
			log.error("获取分类信息失败", e);
			return ResponseBo.error("获取分类信息失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('banner:add')")
	@RequestMapping("/banner/add")
	@ResponseBody
	public ResponseBo addBanner(Banner banner) {
		try {
			this.bannerService.addBanner(banner);
			return ResponseBo.ok("新增轮播图成功！");
		} catch (Exception e) {
			log.error("新增轮播图失败", e);
			return ResponseBo.error("新增轮播图失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('banner:update')")
	@RequestMapping("/banner/update")
	@ResponseBody
	public ResponseBo updateRole(Banner banner) {
		try {
			this.bannerService.updateBanner(banner);
			return ResponseBo.ok("修改轮播图成功！");
		} catch (Exception e) {
			log.error("修改轮播图失败", e);
			return ResponseBo.error("修改轮播图失败，请联系网站管理员！");
		}
	}

	@PreAuthorize("hasAuthority('banner:delete')")
	@RequestMapping("/banner/delete")
	@ResponseBody
	public ResponseBo deleteBanners(String ids) {
		try {
			this.bannerService.deleteBanners(ids);
			return ResponseBo.ok("删除轮播图成功！");
		} catch (Exception e) {
			log.error("删除轮播图失败", e);
			return ResponseBo.error("删除轮播图失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/banner/excel")
	@ResponseBody
	public ResponseBo deptExcel(Banner banner) {
		try {
			List<Banner> list = this.bannerService.getAllBanners();
			return FileUtils.createExcelByPOIKit("轮播图表", list, Banner.class);
		} catch (Exception e) {
			log.error("导出轮播图信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/banner/csv")
	@ResponseBody
	public ResponseBo deptCsv(Banner banner) {
		try {
			List<Banner> list = this.bannerService.getAllBanners();
			return FileUtils.createCsv("轮播图表", list, Banner.class);
		} catch (Exception e) {
			log.error("获取轮播图信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

package com.febs.web.controller.admin.cms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.Setting;
import com.febs.cms.service.SettingService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class SettingController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SettingService settingService;

	@RequestMapping("/setting")
	@PreAuthorize("hasAuthority('setting:list')")
	public String index(Model ui) {
		try {
			String path = ResourceUtils.getURL("classpath:").getPath();
			String cmsPath = path + "templates/themes";
			File templatePath = new File(cmsPath);
			String[] list = templatePath.list();
			ui.addAttribute("templates", list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "admin/cms/setting/setting";
	}

	@RequestMapping("/setting/list")
	@PreAuthorize("hasAuthority('setting:list')")
	@ResponseBody
	public ResponseBo settingList(QueryRequest request, Setting setting) {
		log.debug("查询设置列表");
		try {
			List<Setting> settings= this.settingService.findAllSettings(setting);
			return ResponseBo.ok(settings);
		} catch (Exception e) {
			log.error("获取设置失败", e);
			return ResponseBo.error("获取设置失败！");
		}
	}

	@Log("修改设置")
	@PreAuthorize("hasAuthority('setting:update')")
	@RequestMapping("/setting/update")
	@ResponseBody
	public ResponseBo updateRole(HttpServletRequest request) {
		try {
			List<Setting> settings = this.settingService.findAllSettings(null);
			for (Setting setting : settings) {
				String value = request.getParameter(setting.getKey());
				setting.setValue(value);
				this.settingService.updateByExample(setting);
			}
			return ResponseBo.ok("修改设置成功！");
		} catch (Exception e) {
			log.error("修改文章失败", e);
			return ResponseBo.error("修改设置失败，请联系网站管理员！");
		}
	}
}

package com.febs.web.controller.admin.cms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.febs.cms.domain.Setting;
import com.febs.cms.service.SettingService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.ResponseBo;
import com.febs.web.controller.base.BaseController;
import com.febs.web.utils.PropertiesUtil;

@Controller
@RequestMapping("/admin")
public class ThemeController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Autowired
	private SettingService settingService;

	@Log("设置列表")
	@RequestMapping("/theme")
	@PreAuthorize("hasAuthority('theme:list')")
	public String index(Model ui) {
		try {
			String path = ResourceUtils.getURL("classpath:").getPath();
			String cmsPath = path + "templates/themes";
			File templatePath = new File(cmsPath);
			File[] files = templatePath.listFiles();
			List<Properties> templates = new ArrayList<>();
			for (File themFolder : files) {
				String propes = "templates/themes/" + themFolder.getName() + "/template.properties";
				PropertiesUtil prop = new PropertiesUtil(propes);
				templates.add(prop.getProperties());
			}
			Setting setting = settingService.findByKey("theme");
			ui.addAttribute("templates", templates);
			ui.addAttribute("theme",setting.getValue());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "admin/cms/theme/list";
	}
	@Log("设置更新主题")
	@RequestMapping("/theme/update")
	@PreAuthorize("hasAuthority('theme:update')")
	public void updateTheme(String themeId,HttpServletRequest request, HttpServletResponse response) {
		Setting setting=new Setting();
		setting.setKey("theme");
		setting.setValue(themeId);
		settingService.updateByExample(setting);
		settingService.refreshSetting();
		try {
			redirectStrategy.sendRedirect(request, response, "/admin/theme");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

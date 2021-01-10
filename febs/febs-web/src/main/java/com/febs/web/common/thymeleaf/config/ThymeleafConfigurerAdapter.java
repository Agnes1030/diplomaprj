package com.febs.web.common.thymeleaf.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.cms.domain.NavMenu;
import com.febs.cms.domain.Setting;
import com.febs.cms.service.NavMenuService;
import com.febs.cms.service.SettingService;
import com.febs.common.domain.Tree;

@Component
public class ThymeleafConfigurerAdapter implements WebMvcConfigurer {

	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;
	@Autowired
	private NavMenuService navMenuService;
	@Autowired
	private SettingService settingService;

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		if (thymeleafViewResolver != null) {
			Tree<NavMenu> tree = this.navMenuService.getNavMenuTree();
			Map<String, Object> vars = new HashMap<>(1);
			vars.put("navs", tree);
			List<Setting> settings = settingService.findAllSettings(null);
			for (Setting set : settings) {
				vars.put(set.getKey(), set.getValue());
			}
			thymeleafViewResolver.setStaticVariables(vars);
		}
		WebMvcConfigurer.super.configureViewResolvers(registry);
	}

}

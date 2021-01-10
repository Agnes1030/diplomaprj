package com.febs.test;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import com.febs.FebsApplication;
import com.febs.cms.service.SettingService;
import com.febs.web.utils.PropertiesUtil;

@SpringBootTest(classes = FebsApplication.class)
public class ThemeFileTest {
	@Autowired
	SettingService settingService;

	@Test
	public void testThemeList() {
		try {
			String cmsPath ="/Users/febsuser/workspace/febs/febs-web/src/main/resources/templates/cms";
			File templatePath = new File(cmsPath);
			File[] files = templatePath.listFiles();
			for (File themFolder : files) {
				String propes="templates/cms/"+themFolder.getName()+"/template.properties";
				PropertiesUtil prop=new PropertiesUtil(propes);
				System.out.println("path="+prop.getProperties());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

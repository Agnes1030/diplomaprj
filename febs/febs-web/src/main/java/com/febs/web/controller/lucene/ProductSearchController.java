package com.febs.web.controller.lucene;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.febs.cms.domain.Setting;
import com.febs.cms.service.SettingService;
import com.febs.common.domain.QueryRequest;
import com.febs.web.controller.base.WebBaseController;

@Controller
@RequestMapping("/search")
public class ProductSearchController extends WebBaseController {

	@Autowired
	private SettingService settingService;

	@RequestMapping("/searchProducts")
	public String searchProductByCategory(HttpServletRequest request, QueryRequest queryRequest, String categoryName,
			String title) throws Exception {
		if (queryRequest.getPageNum() == 0) {
			queryRequest.setPageNum(1);
		}
		if (queryRequest.getPageSize() == 0) {
			queryRequest.setPageSize(10);
		}
		if (null != categoryName) {
			request.setAttribute("categoryName", categoryName);
		}
		if (null != title) {
			request.setAttribute("title", title);
		}

		Setting setting = settingService.findByKey("theme");
		// 搜索模板应该放到公共位置比较合理，用户也可以用自定义 的。如果用户没有加入搜索列表页模板，则选择系统默认搜索结果模板
		return "themes/" + setting.getValue() + "/searchList.html";
	}

}

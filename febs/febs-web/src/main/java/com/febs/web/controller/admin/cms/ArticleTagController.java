package com.febs.web.controller.admin.cms;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.ArticleTag;
import com.febs.cms.service.ArticleTagService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class ArticleTagController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ArticleTagService articleTagService;

	@Log("标签列表")
	@RequestMapping("/articleTag")
	@PreAuthorize("hasAuthority('articleTag:list')")
	public String index() {
		return "admin/cms/articleTag/articleTag";
	}

	@Log("获取标签列表信息")
	@RequestMapping("/articleTag/list")
	@PreAuthorize("hasAuthority('articleTag:list')")
	@ResponseBody
	public Map<String, Object> articleList(QueryRequest request, ArticleTag articleTag) {
		log.debug("查询文章列表");
		return super.selectByPageNumSize(request, () -> this.articleTagService.getAllArticleTags(articleTag));
	}

	@RequestMapping("/articleTag/getArticleTag")
	@ResponseBody
	public ResponseBo getArticleTag(Long articleTagId) {
		try {
			ArticleTag articleTag = this.articleTagService.findById(articleTagId);
			return ResponseBo.ok(articleTag);
		} catch (Exception e) {
			log.error("获取分类信息失败", e);
			return ResponseBo.error("获取分类信息失败，请联系网站管理员！");
		}
	}

	@Log("新增标签 ")
	@PreAuthorize("hasAuthority('articleTag:add')")
	@RequestMapping("/articleTag/add")
	@ResponseBody
	public ResponseBo addArticleTag(ArticleTag articleTag) {
		try {

			this.articleTagService.addArticleTag(articleTag);
			return ResponseBo.ok("新增标签成功！");
		} catch (Exception e) {
			log.error("新增标签失败", e);
			return ResponseBo.error("新增标签失败，请联系网站管理员！");
		}
	}

	@Log("修改标签 ")
	@PreAuthorize("hasAuthority('articleTag:update')")
	@RequestMapping("/articleTag/update")
	@ResponseBody
	public ResponseBo updateRole(ArticleTag articleTag) {
		try {
			this.articleTagService.updateArticleTag(articleTag);
			return ResponseBo.ok("修改标签成功！");
		} catch (Exception e) {
			log.error("修改标签失败", e);
			return ResponseBo.error("修改标签失败，请联系网站管理员！");
		}
	}

	@Log("删除标签")
	@PreAuthorize("hasAuthority('articleTag:delete')")
	@RequestMapping("/articleTag/delete")
	@ResponseBody
	public ResponseBo deleteArticleTags(String ids) {
		try {
			this.articleTagService.deleteArticleTags(ids);
			return ResponseBo.ok("删除标签成功！");
		} catch (Exception e) {
			log.error("删除标签失败", e);
			return ResponseBo.error("删除标签失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/articleTag/excel")
	@ResponseBody
	public ResponseBo deptExcel(ArticleTag articleTag) {
		try {
			List<ArticleTag> list = this.articleTagService.getAllArticleTags(articleTag);
			return FileUtils.createExcelByPOIKit("标签表", list, ArticleTag.class);
		} catch (Exception e) {
			log.error("导出标签信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/articleTag/csv")
	@ResponseBody
	public ResponseBo deptCsv(ArticleTag articleTag) {
		try {
			List<ArticleTag> list = this.articleTagService.getAllArticleTags(articleTag);
			return FileUtils.createCsv("标签表", list, ArticleTag.class);
		} catch (Exception e) {
			log.error("获取标签信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

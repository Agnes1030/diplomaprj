package com.febs.web.controller.admin.cms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.Article;
import com.febs.cms.domain.ArticleComment;
import com.febs.cms.service.ArticleCommentService;
import com.febs.common.annotation.Log;
import com.febs.common.domain.QueryRequest;
import com.febs.common.domain.ResponseBo;
import com.febs.common.utils.FileUtils;
import com.febs.system.domain.MyUser;
import com.febs.web.controller.base.BaseController;

@Controller
@RequestMapping("/admin")
public class ArticleCommentController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ArticleCommentService articleCommentService;

	@Log("评论列表")
	@RequestMapping("/articleComment")
	@PreAuthorize("hasAuthority('articleComment:list')")
	public String index() {
		return "admin/cms/articleComment/articleComment";
	}

	@Log("获取文章评论列表信息")
	@RequestMapping("/articleComment/list")
	@PreAuthorize("hasAuthority('articleComment:list')")
	@ResponseBody
	public Map<String, Object> articleCommentList(QueryRequest request, ArticleComment articleComment) {
		log.debug("查询评论列表");
		return super.selectByPageNumSize(request,
				() -> articleCommentService.selectArticleCommentsWithArticle(articleComment.getContent()));
	}

	@RequestMapping("/articleComment/getArticleComment")
	@ResponseBody
	public ResponseBo getArticleComment(Long articleCommentId) {
		try {
			ArticleComment articleComment = this.articleCommentService.findById(articleCommentId);
			return ResponseBo.ok(articleComment);
		} catch (Exception e) {
			log.error("获取文章评论失败", e);
			return ResponseBo.error("获取文章评论失败，请联系网站管理员！");
		}
	}

	@Log("新增文章评论 ")
	@PreAuthorize("hasAuthority('articleComment:add')")
	@RequestMapping("/articleComment/add")
	@ResponseBody
	public ResponseBo addArticleComment(ArticleComment articleComment, HttpServletRequest request) {
		MyUser currentUser = this.getCurrentUser();
		articleComment.setUserId(currentUser.getUserId());
		articleComment.setAuthor(currentUser.getUsername());
		articleCommentService.addArticleComment(articleComment);
		return ResponseBo.ok("新增文章评论成功！");
	}

	@Log("更新文章评论 ")
	@PreAuthorize("hasAuthority('articleComment:update')")
	@RequestMapping("/articleComment/update")
	@ResponseBody
	public ResponseBo updateArticleComment(ArticleComment articleComment, HttpServletRequest request) {
		MyUser currentUser = this.getCurrentUser();
		articleComment.setModifyUid(currentUser.getUserId());
		articleComment.setModifyUserName(currentUser.getUsername());
		articleComment.setModifyTime(new Date());
		articleCommentService.updateArticleComment(articleComment);
		return ResponseBo.ok("更新文章评论成功！");
	}

	@Log("删除文章评论")
	@PreAuthorize("hasAuthority('articleComment:delete')")
	@RequestMapping("/articleComment/delete")
	@ResponseBody
	public ResponseBo deleteArticleComments(String ids) {
		try {
			this.articleCommentService.deleteArticleComments(ids);
			return ResponseBo.ok("删除文章评论成功！");
		} catch (Exception e) {
			log.error("删除文章评论失败", e);
			return ResponseBo.error("删除文章评论失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/articleComment/excel")
	@ResponseBody
	public ResponseBo articleCommentExcel(ArticleComment articleComment) {
		try {
			List<ArticleComment> list = articleCommentService
					.selectArticleCommentsWithArticle(articleComment.getContent());
			return FileUtils.createExcelByPOIKit("文章评论表", list, Article.class);
		} catch (Exception e) {
			log.error("导出文章信息Excel失败", e);
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("/articleComment/csv")
	@ResponseBody
	public ResponseBo articleCommentCsv(ArticleComment articleComment) {
		try {
			List<ArticleComment> list = articleCommentService
					.selectArticleCommentsWithArticle(articleComment.getContent());
			return FileUtils.createCsv("文章评论表", list, Article.class);
		} catch (Exception e) {
			log.error("获取文章信息Csv失败", e);
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
}

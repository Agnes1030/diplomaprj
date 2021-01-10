package com.febs.web.controller.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.cms.domain.ArticleComment;
import com.febs.cms.service.ArticleCommentService;
import com.febs.common.domain.ResponseBo;
import com.febs.security.xss.JsoupUtil;
import com.febs.web.controller.base.WebBaseController;

@Controller
public class CmsArticleController extends WebBaseController {
	@Autowired
	private ArticleCommentService articleCommentService;

	@RequestMapping(value = "/articleComment/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseBo logoUpload(ArticleComment articleComment) {
		String filterContent = JsoupUtil.clean(articleComment.getContent());
		articleComment.setContent(filterContent);
		articleCommentService.addArticleComment(articleComment);
		return ResponseBo.ok("新增文章评论成功！");
	}

}

package com.febs.cms.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.cms.domain.ArticleComment;
import com.febs.cms.service.ArticleCommentService;
import com.febs.common.service.impl.BaseService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ArticleCommentServiceImpl extends BaseService<ArticleComment> implements ArticleCommentService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ArticleComment findById(Long commentId) {
		return this.selectByKey(commentId);
	}

	@Override
	public List<ArticleComment> findAllArticleComments(ArticleComment articleComment) {
		log.info("过滤查询评论列表");
		Example example = new Example(ArticleComment.class);
		Criteria citeria = example.createCriteria();
		if (StringUtils.isNotBlank(articleComment.getContent())) {
			citeria.andLike("content", "%" + articleComment.getContent() + "%");
		}
		if (null != articleComment.getArticleId()) {
			citeria.andEqualTo("articleId", articleComment.getArticleId());
		}
		example.setOrderByClause("seq_num asc");
		return this.selectByExample(example);
	}

	@Override
	public void deleteArticleComments(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		this.batchDelete(list, "id", ArticleComment.class);
	}

	@Override
	public int addArticleComment(ArticleComment articleComment) {
		articleComment.setCreateTime(new Date());
		return this.save(articleComment);
	}

	@Override
	public int updateArticleComment(ArticleComment articleComment) {
		return this.updateNotNull(articleComment);
	}

	@Override
	public List<ArticleComment> selectArticleCommentsWithArticle(String content) {
		Example commentExample = new Example(ArticleComment.class);
		commentExample.createCriteria().andLike("content", "%" + content + "%");
		return this.selectByExample(commentExample);
	}
}

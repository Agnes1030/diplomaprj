package com.febs.cms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.cms.dao.ArticleMapper;
import com.febs.cms.domain.Article;
import com.febs.cms.service.ArticleService;
import com.febs.common.service.impl.BaseService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ArticleServiceImpl extends BaseService<Article> implements ArticleService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	ArticleMapper articleMapper;

	@Override
	public Article findById(Long articleId) {
		return this.selectByKey(articleId);
	}

	@Override
	public List<Article> findAllArticles(Article article, List<Long> ids) {
		try {
			return this.articleMapper.queryArticles(article, ids);
		} catch (Exception e) {
			log.error("获取文章列表失败", e);
			return new ArrayList<>();
		}

	}

	@Override
	@Transactional
	public void updateArticle(Article article) {
		this.updateNotNull(article);
	}

	@Override
	@Transactional
	public void addArticle(Article article) {
		article.setCreateTime(new Date());
		this.save(article);
	}

	@Override
	@Transactional
	public void deleteArticles(String articleIds) {
		List<String> list = Arrays.asList(articleIds.split(","));
		this.batchDelete(list, "id", Article.class);
	}

	@Override
	public void updateViewCount(Long id) {
		articleMapper.updateViewCount(id);
	}

	@Override
	public List<Article> findHotArticles(Long categoryId) {
		try {
			List<Long> ids = new ArrayList<Long>();
			ids.add(categoryId);
			return this.articleMapper.queryArticles(null, ids);
		} catch (Exception e) {
			log.error("获取文章列表失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	public List<Article> findUserArticles(Long userId) {
		Example example = new Example(Article.class);
		example.createCriteria().andEqualTo("userId", userId);
		example.setOrderByClause("create_time desc");
		return this.articleMapper.selectByExample(example);
	}

	@Override
	public Article findBySlug(String slug) {
		Example example = new Example(Article.class);
		example.createCriteria().andEqualTo("slug", slug);
		List<Article> list = this.selectByExample(example);
		return list.size() == 0 ? null : list.get(0);
	}

	@Override
	public Article findByIdSlug(String idSlug) {
		if (StringUtils.isNumeric(idSlug)) {
			return this.articleMapper.selectByPrimaryKey(idSlug);
		} else {
			return this.findBySlug(idSlug);
		}
	}
}

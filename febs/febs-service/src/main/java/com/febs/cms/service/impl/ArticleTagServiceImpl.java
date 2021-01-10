package com.febs.cms.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.febs.cms.dao.ArticleTagMapper;
import com.febs.cms.domain.ArticleTag;
import com.febs.cms.service.ArticleTagService;
import com.febs.common.service.impl.BaseService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ArticleTagServiceImpl extends BaseService<ArticleTag> implements ArticleTagService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ArticleTagMapper articleTagMapper;

	@Override
	public ArticleTag findById(Long articleTagId) {
		return this.selectByKey(articleTagId);
	}

	@Override
	public void updateArticleTag(ArticleTag articleTag) {
		log.debug("更新标签:" + articleTag);
		this.updateNotNull(articleTag);
	}

	@Override
	public void addArticleTag(ArticleTag articleTag) {
		articleTag.setCreateTime(new Date());
		this.save(articleTag);
	}

	@Override
	public void deleteArticleTags(String articleTagIds) {
		List<String> list = Arrays.asList(articleTagIds.split(","));
		this.batchDelete(list, "id", ArticleTag.class);
	}

	@Override
	public List<ArticleTag> getAllArticleTags(ArticleTag articleTag) {
		Example example = new Example(ArticleTag.class);
		Criteria criteria = example.createCriteria();
		if (null != articleTag && !StringUtils.isEmpty(articleTag.getTagName())) {
			criteria.andCondition("lower(tag_name)=", articleTag.getTagName());
		}
		return this.selectByExample(example);
	}

	public List<ArticleTag> getTagByName(String tagName) {
		Example example = new Example(ArticleTag.class);
		Criteria criteria = example.createCriteria();
		criteria.andCondition("lower(tag_name)=", tagName);
		return this.selectByExample(example);
	}

	public List<ArticleTag> getTagByArticleIds(String articleIds) {
		List<String> list = Arrays.asList(articleIds.split(","));
		return articleTagMapper.getArticleTags(list);
	}

}

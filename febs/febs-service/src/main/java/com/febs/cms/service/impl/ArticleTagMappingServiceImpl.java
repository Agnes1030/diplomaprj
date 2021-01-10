package com.febs.cms.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.cms.dao.ArticleTagMappingMapper;
import com.febs.cms.domain.ArticleTagMapping;
import com.febs.cms.service.ArticleTagMappingService;
import com.febs.common.service.impl.BaseService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ArticleTagMappingServiceImpl extends BaseService<ArticleTagMapping> implements ArticleTagMappingService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ArticleTagMappingMapper articleTagMappingMapper;

	public Integer queryArticleCounts(Long tagId) {
		return articleTagMappingMapper.queryArticleCounts(tagId);
	}

	public Integer deleteArticleTagMapping(Long articleId) {
		log.debug("删除标签文章关系articleId=" + articleId);
		// 删除文章的所有关联关系
		Example example = new Example(ArticleTagMapping.class);
		example.createCriteria().andEqualTo("articleId", articleId);

		return this.getMapper().deleteByExample(example);
	}

	public Integer batchDeleteTagMapping(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		return this.batchDelete(list, "articleId", ArticleTagMapping.class);
	}

}

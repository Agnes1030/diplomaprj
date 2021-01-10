package com.febs.cms.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.cms.domain.ArticleCategoryMapping;
import com.febs.cms.service.ArticleCategoryMappingService;
import com.febs.common.service.impl.BaseService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ArticleCategoryMappingServiceImpl extends BaseService<ArticleCategoryMapping>
		implements ArticleCategoryMappingService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Integer deleteArticleCategoryMapping(Long articleId) {
		log.debug("删除标签文章关系articleId=" + articleId);
		// 删除文章的所有关联关系
		Example example = new Example(ArticleCategoryMapping.class);
		example.createCriteria().andEqualTo("articleId", articleId);

		return this.getMapper().deleteByExample(example);
	}

	@Override
	public Integer batchDeleteCategoryMapping(String articleIds) {
		List<String> list = Arrays.asList(articleIds.split(","));
		return this.batchDelete(list, "articleId", ArticleCategoryMapping.class);
	}

	@Override
	public Integer deleteCategoryMappingByCat(String categoryIds) {
		List<String> list = Arrays.asList(categoryIds.split(","));
		return this.batchDelete(list, "categoryId", ArticleCategoryMapping.class);
	}

}

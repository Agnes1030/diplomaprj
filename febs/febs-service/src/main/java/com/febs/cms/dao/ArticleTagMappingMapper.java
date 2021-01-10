package com.febs.cms.dao;

import com.febs.cms.domain.ArticleTagMapping;
import com.febs.common.config.MyMapper;

public interface ArticleTagMappingMapper extends MyMapper<ArticleTagMapping> {
	public Integer queryArticleCounts(Long tagId);

}

package com.febs.cms.dao;

import java.util.List;

import com.febs.cms.domain.ArticleTag;
import com.febs.common.config.MyMapper;

public interface ArticleTagMapper extends MyMapper<ArticleTag> {
	/*
	 * 根据文章ID列表查询所关联的标签
	 */
	public List<ArticleTag> getArticleTags(List<String> articleIds); 

}

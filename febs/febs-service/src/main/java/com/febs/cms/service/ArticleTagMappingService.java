package com.febs.cms.service;

import com.febs.cms.domain.ArticleTagMapping;
import com.febs.common.service.IService;

public interface ArticleTagMappingService extends IService<ArticleTagMapping> {
	public Integer queryArticleCounts(Long tagId);
	public Integer deleteArticleTagMapping(Long articleId);
	public Integer batchDeleteTagMapping(String ids);

}

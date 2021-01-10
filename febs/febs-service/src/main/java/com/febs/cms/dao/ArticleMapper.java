package com.febs.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.febs.cms.domain.Article;
import com.febs.common.config.MyMapper;

public interface ArticleMapper extends MyMapper<Article> {
	public List<Article> queryArticles(@Param("article") Article article, @Param("ids") List<Long> ids);

	public void updateViewCount(Long id);
	@Select("select content from t_article where id=#{id}")
	public String getArticleContent(Long id);

}

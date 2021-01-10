package com.febs.cms.dao;

import java.util.List;

import com.febs.cms.domain.SinglePage;
import com.febs.common.config.MyMapper;

public interface SinglePageMapper extends MyMapper<SinglePage> {
	public List<SinglePage> querySinglePages(SinglePage singlePage);

}

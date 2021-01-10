package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.SinglePage;
import com.febs.common.service.IService;

public interface SinglePageService extends IService<SinglePage> {
	SinglePage findById(Long singlePageId);
	public void updateSinglePage(SinglePage singlePage);
	public void addSinglePage(SinglePage singlePage);
	public void deleteSinglePages(String singlePageIds);
	public List<SinglePage> findAllSinglePages(SinglePage singlePage);
	public SinglePage findBySlug(String slug);
}

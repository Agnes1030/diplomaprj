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

import com.febs.cms.dao.SinglePageMapper;
import com.febs.cms.domain.SinglePage;
import com.febs.cms.service.SinglePageService;
import com.febs.common.service.impl.BaseService;
import com.febs.common.utils.PinyinUtil;

import tk.mybatis.mapper.entity.Example;

@Service("singlePageService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SinglePageServiceImpl extends BaseService<SinglePage> implements SinglePageService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SinglePageMapper singlePageMapper;

	@Override
	public SinglePage findById(Long singlePageId) {
		return this.selectByKey(singlePageId);
	}

	@Override
	public SinglePage findBySlug(String slug) {
		SinglePage singlePage = new SinglePage();
		singlePage.setSlug(slug);
		List<SinglePage> pages = this.findAllSinglePages(singlePage);
		if (null != pages && pages.size() > 0) {
			return pages.get(0);
		}
		return null;
	}

	@Override
	public List<SinglePage> findAllSinglePages(SinglePage singlePage) {
		try {
			Example example = new Example(SinglePage.class);
			if (StringUtils.isNotBlank(singlePage.getTitle())) {
				example.createCriteria().andLike("title", "%" + singlePage.getTitle() + "%");
			}
			if (StringUtils.isNotBlank(singlePage.getSlug())) {
				example.createCriteria().andEqualTo("slug", singlePage.getSlug());
			}

			if (StringUtils.isNotBlank(singlePage.getContent())) {
				example.createCriteria().andLike("content", "%" + singlePage.getContent() + "%");
			}
			if (StringUtils.isNotBlank(singlePage.getStatus())) {
				example.createCriteria().andEqualTo("status", singlePage.getStatus());
			}
			return this.selectByExample(example);
		} catch (Exception e) {
			log.error("获取单页列表失败", e);
			return new ArrayList<>();
		}

	}

	@Override
	@Transactional
	public void updateSinglePage(SinglePage singlePage) {
		if (StringUtils.isEmpty(singlePage.getSlug())) {
			String slug = PinyinUtil.getFullPinyin(singlePage.getTitle());
			singlePage.setSlug(slug);
		}
		this.updateNotNull(singlePage);
	}

	@Override
	@Transactional
	public void addSinglePage(SinglePage singlePage) {
		if (StringUtils.isEmpty(singlePage.getSlug())) {
			String slug = PinyinUtil.getFullPinyin(singlePage.getTitle());
			singlePage.setSlug(slug);
		}
		singlePage.setCreateTime(new Date());
		this.save(singlePage);
	}

	@Override
	@Transactional
	public void deleteSinglePages(String singlePageIds) {
		List<String> list = Arrays.asList(singlePageIds.split(","));
		this.batchDelete(list, "id", SinglePage.class);
	}
}

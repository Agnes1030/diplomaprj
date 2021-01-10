package com.febs.cms.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.cms.dao.BannerMapper;
import com.febs.cms.domain.Banner;
import com.febs.cms.service.BannerService;
import com.febs.common.service.impl.BaseService;

@Service("bannerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BannerServiceImpl extends BaseService<Banner> implements BannerService {
	@Autowired
	private BannerMapper bannerMapper;

	@Override
	public Banner findById(Long bannerId) {
		return this.selectByKey(bannerId);
	}

	@Override
	public void updateBanner(Banner banner) {
		this.updateNotNull(banner);
	}

	@Override
	public void addBanner(Banner banner) {
		banner.setCreateTime(new Date());
		this.save(banner);
	}

	@Override
	public void deleteBanners(String bannerIds) {
		List<String> list = Arrays.asList(bannerIds.split(","));
		this.batchDelete(list, "id", Banner.class);
	}

	@Override
	public List<Banner> getAllBanners() {
		return this.selectAll();
	}

}

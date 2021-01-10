package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.Banner;
import com.febs.common.service.IService;

public interface BannerService extends IService<Banner> {
	Banner findById(Long bannerId);
	public void updateBanner(Banner banner);
	public void addBanner(Banner banner);
	public void deleteBanners(String bannerIds);
	public List<Banner> getAllBanners();
}

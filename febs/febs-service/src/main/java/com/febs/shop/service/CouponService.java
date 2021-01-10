package com.febs.shop.service;

import com.febs.common.service.IService;
import com.febs.shop.domain.Coupon;

public interface CouponService extends IService<Coupon> {
	public Integer updateTakeCount(Integer takeCount, Long couponId);

	public Integer updateUsedCount(Integer usedCount, Long couponId);

}

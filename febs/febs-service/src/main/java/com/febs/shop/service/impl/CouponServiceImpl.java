package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.CouponMapper;
import com.febs.shop.domain.Coupon;
import com.febs.shop.service.CouponService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CouponServiceImpl extends BaseService<Coupon> implements CouponService {
	@Autowired
	private CouponMapper couponMapper;

	@Override
	public Integer updateTakeCount(Integer takeCount, Long couponId) {
		return couponMapper.updateTakeCount(takeCount, couponId);
	}

	@Override
	public Integer updateUsedCount(Integer usedCount, Long couponId) {
		return couponMapper.updateUsedCount(usedCount, couponId);
	}

}

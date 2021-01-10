package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.CouponUserMapper;
import com.febs.shop.domain.CouponUser;
import com.febs.shop.service.CouponUserService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CouponUserServiceImpl extends BaseService<CouponUser> implements CouponUserService {
	@Autowired
	private CouponUserMapper couponUserMapper;

}

package com.febs.shop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.domain.UserAddress;
import com.febs.shop.service.UserAddressService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserAddressServiceImpl extends BaseService<UserAddress> implements UserAddressService {

	@Override
	public List<UserAddress> findUserAddress(Long userId) {
		Example example = new Example(UserAddress.class);
		example.createCriteria().andEqualTo("userId", userId);
		return this.selectByExample(example);
	}

	@Override
	public int deleteUserAddress(String addressIds) {
		List<String> list = Arrays.asList(addressIds.split(","));
		return this.batchDelete(list, "id", UserAddress.class);
	}

}

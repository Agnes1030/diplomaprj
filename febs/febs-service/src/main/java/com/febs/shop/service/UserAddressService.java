package com.febs.shop.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.shop.domain.UserAddress;

public interface UserAddressService extends IService<UserAddress> {
	public int deleteUserAddress(String addressIds);
	public List<UserAddress> findUserAddress(Long userId);

}

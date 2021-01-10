package com.febs.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.member.dao.UserFavouriteMapper;
import com.febs.member.domain.UserFavourite;
import com.febs.member.service.UserFavouriteService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserFavouriteServiceImpl extends BaseService<UserFavourite> implements UserFavouriteService {
	@Autowired
	private UserFavouriteMapper userFavouriteMapper;

}

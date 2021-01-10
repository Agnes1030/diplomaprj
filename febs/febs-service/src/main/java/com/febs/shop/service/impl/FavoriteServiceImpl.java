package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.FavoriteMapper;
import com.febs.shop.domain.Favorite;
import com.febs.shop.service.FavoriteService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FavoriteServiceImpl extends BaseService<Favorite> implements FavoriteService {
	@Autowired
	private FavoriteMapper favoriteMapper;

}

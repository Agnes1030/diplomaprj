package com.febs.cms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.cms.dao.FriendLinkMapper;
import com.febs.cms.domain.FriendLink;
import com.febs.cms.service.FriendLinkService;
import com.febs.common.service.impl.BaseService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("friendLinkService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FriendLinkServiceImpl extends BaseService<FriendLink> implements FriendLinkService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	FriendLinkMapper friendLinkMapper;
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;

	@Override
	public void refreshLinks() {
		if (thymeleafViewResolver != null) {
			List<FriendLink> links = findAllFriendLinks(null);
			Map<String, Object> vars = new HashMap<>(1);
			vars.put("friendLinks", links);
			thymeleafViewResolver.setStaticVariables(vars);
		}
	}

	@Override
	public List<FriendLink> findAllFriendLinks(FriendLink friendLink) {
		try {
			Example example = new Example(FriendLink.class);
			Criteria criteria = example.createCriteria();
			if (null != friendLink && StringUtils.isNotBlank(friendLink.getName())) {
				criteria.andCondition("name like", "%" + friendLink.getName() + "%");
			}
			return this.selectByExample(example);
		} catch (Exception e) {
			log.error("获取分类列表失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	public FriendLink findByName(String friendLinkName) {
		Example example = new Example(FriendLink.class);
		example.createCriteria().andCondition("lower(name) =", friendLinkName.toLowerCase());
		List<FriendLink> list = this.selectByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public FriendLink findById(Long friendLinkId) {
		return this.selectByKey(friendLinkId);
	}

	@Override
	@Transactional
	public void addFriendLink(FriendLink friendLink) {
		friendLink.setCreateTime(new Date());
		this.save(friendLink);
		this.refreshLinks();
	}

	@Override
	@Transactional
	public void updateFriendLink(FriendLink friendLink) {
		this.updateNotNull(friendLink);
		this.refreshLinks();
	}

	@Override
	@Transactional
	public void deleteFriendLinks(String navIds) {
		List<String> list = Arrays.asList(navIds.split(","));
		this.batchDelete(list, "id", FriendLink.class);
		this.refreshLinks();

	}
}

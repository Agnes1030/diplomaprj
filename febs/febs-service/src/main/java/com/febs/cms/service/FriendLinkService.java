package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.FriendLink;
import com.febs.common.service.IService;

public interface FriendLinkService extends IService<FriendLink> {

	List<FriendLink> findAllFriendLinks(FriendLink friendLink);

	FriendLink findByName(String friendLinkName);

	FriendLink findById(Long friendLinkId);

	void addFriendLink(FriendLink friendLink);

	void updateFriendLink(FriendLink friendLink);

	void deleteFriendLinks(String friendLinkIds);

	void refreshLinks();

}

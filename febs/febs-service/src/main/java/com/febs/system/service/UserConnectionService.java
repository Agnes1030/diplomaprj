package com.febs.system.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.system.domain.UserConnection;

public interface UserConnectionService extends IService<UserConnection> {

    boolean isExist(String userId, String providerId);

    List<UserConnection> findByProviderUserId(String providerUserId);

    void delete(UserConnection userConnection);
}

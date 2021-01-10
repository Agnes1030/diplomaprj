package com.febs.security.social.weixin.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import com.febs.security.social.weixin.api.WeiXin;
import com.febs.security.social.weixin.api.WeiXinUserInfo;

public class WeiXinAdapter implements ApiAdapter<WeiXin> {

    private String openId;

    public WeiXinAdapter() {
    }

    public WeiXinAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(WeiXin api) {
        return true;
    }

    @Override
    public void setConnectionValues(WeiXin api, ConnectionValues values) {
        WeiXinUserInfo profile = api.getUserInfo(openId);
        values.setProviderUserId(profile.getOpenid());
        values.setDisplayName(profile.getNickname());
        values.setImageUrl(profile.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(WeiXin api) {
        return null;
    }

    @Override
    public void updateStatus(WeiXin api, String message) {
        //do nothing
    }

}

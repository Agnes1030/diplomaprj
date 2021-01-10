package com.febs.security.social.weixin.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

import com.febs.common.domain.FebsConstant;
import com.febs.security.social.weixin.api.WeiXin;
import com.febs.security.social.weixin.api.WeiXinImpl;

public class WeiXinServiceProvider extends AbstractOAuth2ServiceProvider<WeiXin> {

    public WeiXinServiceProvider(String appId, String appSecret) {
        super(new WeiXinOAuth2Template(appId, appSecret, FebsConstant.WEIXIN_AUTHORIZE_URL, FebsConstant.GET_WEIXIN_ACCESSTOKEN_URL));
    }

    @Override
    public WeiXin getApi(String accessToken) {
        return new WeiXinImpl(accessToken);
    }

}

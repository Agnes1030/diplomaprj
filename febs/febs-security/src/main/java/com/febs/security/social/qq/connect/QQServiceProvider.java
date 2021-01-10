package com.febs.security.social.qq.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

import com.febs.common.domain.FebsConstant;
import com.febs.security.social.qq.api.QQ;
import com.febs.security.social.qq.api.QQImpl;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, FebsConstant.QQ_AUTHORIZE_URL, FebsConstant.GET_QQ_ACCESSTOKEN_URL));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}

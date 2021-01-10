package com.febs.security.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import com.febs.security.handler.FebsAuthenticationSucessHandler;
import com.febs.security.properties.FebsSecurityProperties;

public class FebsSpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    private FebsSecurityProperties febsSecurityProperties;

    private FebsAuthenticationSucessHandler febsAuthenticationSucessHandler;

    public FebsSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter socialAuthenticationFilter = (SocialAuthenticationFilter)super.postProcess(object);
        socialAuthenticationFilter.setFilterProcessesUrl(filterProcessesUrl);
        socialAuthenticationFilter.setSignupUrl(febsSecurityProperties.getSocial().getSocialRedirectUrl());
        socialAuthenticationFilter.setAuthenticationSuccessHandler(febsAuthenticationSucessHandler);
        return (T) socialAuthenticationFilter;
    }

    public FebsSecurityProperties getFebsSecurityProperties() {
        return febsSecurityProperties;
    }

    public void setFebsSecurityProperties(FebsSecurityProperties febsSecurityProperties) {
        this.febsSecurityProperties = febsSecurityProperties;
    }

    public FebsAuthenticationSucessHandler getFebsAuthenticationSucessHandler() {
        return febsAuthenticationSucessHandler;
    }

    public void setFebsAuthenticationSucessHandler(FebsAuthenticationSucessHandler febsAuthenticationSucessHandler) {
        this.febsAuthenticationSucessHandler = febsAuthenticationSucessHandler;
    }

}

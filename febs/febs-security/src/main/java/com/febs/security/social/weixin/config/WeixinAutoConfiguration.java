package com.febs.security.social.weixin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

import com.febs.security.properties.FebsSecurityProperties;
import com.febs.security.properties.WeiXinProperties;
import com.febs.security.social.SocialConnectedView;
import com.febs.security.social.weixin.connect.WeiXinConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "febs.security.social.weixin", name = "app-id")
@Order(2)
public class WeixinAutoConfiguration extends SocialConfigurerAdapter {

	@Autowired
	private FebsSecurityProperties securityProperties;

	protected ConnectionFactory<?> createConnectionFactory() {
		WeiXinProperties weixinConfig = securityProperties.getSocial().getWeixin();
		return new WeiXinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
				weixinConfig.getAppSecret());
	}

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
			Environment environment) {
		super.addConnectionFactories(connectionFactoryConfigurer, environment);
		WeiXinProperties weixinConfig = securityProperties.getSocial().getWeixin();
		WeiXinConnectionFactory weixinConnectionFactory = new WeiXinConnectionFactory(weixinConfig.getProviderId(),
				weixinConfig.getAppId(), weixinConfig.getAppSecret());
		connectionFactoryConfigurer.addConnectionFactory(weixinConnectionFactory);

	}

	@Bean({ "connect/weixinConnect", "connect/weixinConnected" })
	public View weixinConnectedView() {
		return new SocialConnectedView();
	}
}

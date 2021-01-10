package com.febs.security.social.qq.config;

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
import com.febs.security.properties.QQProperties;
import com.febs.security.social.SocialConnectedView;
import com.febs.security.social.qq.connect.QQConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "febs.security.social.qq", name = "app-id")
@Order(2)
public class QQAutoConfig extends SocialConfigurerAdapter {

	@Autowired
	private FebsSecurityProperties securityProperties;

	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties properties = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(properties.getProviderId(), properties.getAppId(), properties.getAppSecret());
	}

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
			Environment environment) {
		super.addConnectionFactories(connectionFactoryConfigurer, environment);
		QQProperties properties = securityProperties.getSocial().getQq();

		QQConnectionFactory qqConnectionFactory = new QQConnectionFactory(properties.getProviderId(),
				properties.getAppId(), properties.getAppSecret());
		connectionFactoryConfigurer.addConnectionFactory(qqConnectionFactory);
	}

	@Bean({ "connect/qqConnect", "connect/qqConnected" })
	public View qqConnectedView() {
		return new SocialConnectedView();
	}
}

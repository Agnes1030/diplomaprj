package com.febs.web.common.thymeleaf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.febs.web.common.thymeleaf.dialect.CmsDialect;
import com.febs.web.common.thymeleaf.dialect.DictDialect;

/**
 * 自定义字典标签控制类
 *
 * @author lzx
 * @date 2018年11月26日20:26:42
 *
 */
@Configuration
public class DirectiveConfig {

	@Bean
	public DictDialect getDictDialect() {
		return new DictDialect();
	}

	@Bean
	public CmsDialect getCmsDialect() {
		return new CmsDialect();
	}
}

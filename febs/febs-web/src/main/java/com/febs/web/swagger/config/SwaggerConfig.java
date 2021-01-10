package com.febs.web.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				// swagger要扫描的包路径
				.apis(RequestHandlerSelectors.basePackage("com.febs.web.controller.admin")).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("febs Swagger").description("febs 整合Swagger2 API展示")
				.termsOfServiceUrl("localhost:8081")
				.contact(new Contact("Swagger测试", "localhost:8081/swagger-ui.html", "febs@qq.com")).version("1.0")
				.build();
	}

}
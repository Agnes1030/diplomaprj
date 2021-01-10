package com.febs;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.febs.common.properties.FebsProperies;
import com.febs.listener.CloseListener;
import com.febs.security.properties.FebsSecurityProperties;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan({ "com.febs.*.dao" })
@EnableConfigurationProperties({ FebsSecurityProperties.class, FebsProperies.class })
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class FebsApplication {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(FebsApplication.class);
		application.addListeners(new CloseListener());
		application.run(args);
		LoggerFactory.getLogger(FebsApplication.class).info("《《《《《《 FEBS started up successfully at {} {} 》》》》》》",
				LocalDate.now(), LocalTime.now());
	}
}

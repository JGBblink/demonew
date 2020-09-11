package com.example.my_starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动扫描加载类
 * @author JGB
 */
@Configuration
@EnableConfigurationProperties(value = {MyConfigurationProperties.class})
public class MyAutoConfigStarter {

	static {
		System.out.println("自定义加载模块被加载");
	}

	@Bean
	@ConditionalOnProperty(name = "my.starter.enable", havingValue = "true")
	public MyConfig instanceMyConfig() {
		System.out.println("------------------------------->加载配置bean");
		return new MyConfig();
	}
}

package com.example.my_starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("my.starter")
public class MyConfigurationProperties {

	/**
	 * 开启自动加载
	 */
	private Boolean enable = false;

	/**
	 * 模块名称
	 */
	private String moduleName = "my_starter";

	/**
	 * 定时器表达式
	 */
	private String cron;
}

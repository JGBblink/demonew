package com.example.demo_test.config;

import org.springframework.boot.test.context.TestConfiguration;

/**
 * 单元测试:需要引入的配置文件
 *
 * @author JGB
 */
@TestConfiguration
public class TestWebSecurityConfigurer {

	{
		System.out.println("单元测试配置文件被加载");
	}

}

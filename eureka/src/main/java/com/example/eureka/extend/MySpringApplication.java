package com.example.eureka.extend;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class MySpringApplication extends SpringApplication {

	public MySpringApplication(Class<?>... primarySources) {
		super(primarySources);
	}

	@Override
	protected void afterRefresh(ConfigurableApplicationContext context, ApplicationArguments args) {

		System.out.println("-------执行自定义的application");
	}
}

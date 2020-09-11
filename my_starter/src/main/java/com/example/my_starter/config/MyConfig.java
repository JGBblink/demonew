package com.example.my_starter.config;

import org.springframework.scheduling.annotation.Scheduled;

public class MyConfig {

	static {
		System.out.println("加载MyConfig");
	}

	@Scheduled(cron = "${my.starter.cron}")
	public void run() {
		System.out.println(System.currentTimeMillis());
	}
}

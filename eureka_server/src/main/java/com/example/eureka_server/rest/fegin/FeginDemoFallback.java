package com.example.eureka_server.rest.fegin;

import org.springframework.stereotype.Component;

@Component
public class FeginDemoFallback implements FeginDemoClient{
	@Override
	public String feginRemoteInvoke(long time) {
		throw new RuntimeException("微服务调用出错");
	}
}

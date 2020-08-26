package com.example.eureka_server.sdk;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeginDemoFallback implements FallbackFactory<FeginDemoClient> {

	@Override
	public FeginDemoClient create(Throwable throwable) {
		return new FeginDemoClient() {
			@Override
			public String feginRemoteInvoke(long time) {
				//throw new RuntimeException("微服务调用出错");
				return "微服务调用出错";
			}
		};
	}
}

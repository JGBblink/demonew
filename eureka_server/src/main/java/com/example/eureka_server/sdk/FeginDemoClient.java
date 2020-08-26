package com.example.eureka_server.sdk;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "eureka-server",path = "/fegin",fallbackFactory = FeginDemoFallback.class)
public interface FeginDemoClient {

	@GetMapping("/private/demo")
	String feginRemoteInvoke(@RequestParam("time") long time);
}

package com.example.eureka_server.rest.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "eureka-server",path = "/fegin",fallback = FeginDemoFallback.class)
public interface FeginDemoClient {

	@GetMapping("/demo")
	String feginRemoteInvoke(@RequestParam("time") long time);
}

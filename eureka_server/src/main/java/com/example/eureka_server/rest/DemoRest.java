package com.example.eureka_server.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoRest {

	/**
	 * 测试ribbon通过restTemplate调用
	 * @return
	 */
	@GetMapping("/ribbon/template")
	public String testRibbonForTemplate() {
		return "this is ribbon for restTemplate remote invoke!";
	}

	/**
	 * 测试hyxtrix熔断器
	 * @return
	 */
	@GetMapping("/hystrix")
	@HystrixCommand(fallbackMethod = "fallback")
	public String hystrixDemo(Long time) throws InterruptedException {
		Thread.sleep(time);
		return "hello hystrix!";
	}

	public String fallback(Long time) {
		return "fallback" + time;
	}

}

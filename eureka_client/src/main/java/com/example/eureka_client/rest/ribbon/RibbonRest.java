package com.example.eureka_client.rest.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/ribbon")
public class RibbonRest {

	@Autowired
	RestTemplate restTemplate;

	/**
	 * 测试ribbon通过restTemplate调用
	 * @return
	 */
	@GetMapping("/template")
	public String testRibbonForTemplate() {
		String result = restTemplate.getForEntity("http://eureka-server/demo/ribbon/template", String.class).getBody();
		//String result = restTemplate.getForEntity("http://localhost:9020/demo/ribbon/template", String.class).getBody();
		return result;
	}

	/**
	 * 调用
	 * @return
	 */
	@GetMapping("/hystrix")
	public String hystrix(long time) {
		String result = restTemplate.getForEntity("http://eureka-server/demo/hystrix?time=" + time, String.class).getBody();
		//String result = restTemplate.getForEntity("http://localhost:9020/demo/ribbon/template", String.class).getBody();
		return result;
	}
}

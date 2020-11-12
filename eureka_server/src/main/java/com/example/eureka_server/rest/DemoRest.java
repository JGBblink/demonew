package com.example.eureka_server.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/httpclient/start")
	public ResponseEntity httpclientTest(@RequestParam String id, @RequestParam Long stopTime) throws InterruptedException {
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, String> params = new LinkedMultiValueMap();
		params.add("id",id);
		params.add("stopTime",stopTime + "");
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

		Map<String,String> uriParams = new HashMap<>(3);
		uriParams.put("id",id);
		uriParams.put("stopTime",stopTime + "");
		ResponseEntity<String> exchange = restTemplate.exchange("http://127.0.0.1:9010/demo/httpclient/demo?id={id}&stopTime={stopTime}", HttpMethod.GET, requestEntity, String.class,uriParams);
		return exchange;
	}

}

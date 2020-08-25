package com.example.eureka_client.rest.fegin;

import com.example.eureka_server.rest.fegin.FeginDemoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fegin")
public class FeginDemoRest {

	@Autowired
	FeginDemoClient feginDemoClient;

	/**
	 * fegin远程调用示例
	 * @return
	 */
	@GetMapping("/demo")
	public String feginRemoteInvoke(long time) {
		String s = feginDemoClient.feginRemoteInvoke(time);
		return s;
	}
}

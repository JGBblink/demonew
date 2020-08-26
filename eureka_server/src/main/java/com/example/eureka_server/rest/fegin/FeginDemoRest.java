package com.example.eureka_server.rest.fegin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fegin")
public class FeginDemoRest {

	/**
	 * fegin远程调用示例
	 * @return
	 */
	@GetMapping("/private/demo")
	public String feginRemoteInvoke(long time) {
		System.out.println("进入fegin远程调用");
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "hello fegin!";
	}
}

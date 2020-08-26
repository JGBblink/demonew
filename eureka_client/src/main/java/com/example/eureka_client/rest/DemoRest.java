package com.example.eureka_client.rest;

import com.example.eureka_client.config.HealthMonitorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/demo")
public class DemoRest {

	@Autowired
	private HealthMonitorConfig healthMonitorConfig;

	@Autowired
	DiscoveryClient discoveryClient;

	/**
	 * 动态更改当前服务状态
	 * @param status
	 * @return
	 */
	@GetMapping("/change/servers/status")
	public String changeServerStatus(Boolean status) {
		if(status != null) {
			healthMonitorConfig.setStatus(status);
		}
		return "success";
	}

	/**
	 * 获取eureka-client服务列表信息
	 * @return
	 */
	@GetMapping("/servers/info")
	public List<ServiceInstance> getServersInfo() {

		List<ServiceInstance> collect = discoveryClient.getServices().stream()
				.map(sid -> discoveryClient.getInstances(sid))
				.collect(Collectors.toList())
				.stream()
				.flatMap(list -> list.stream())
				.collect(Collectors.toList());

		return collect;
	}
}

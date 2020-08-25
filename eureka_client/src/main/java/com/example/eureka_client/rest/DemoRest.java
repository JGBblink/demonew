package com.example.eureka_client.rest;

import com.example.eureka_client.config.HealthMonitorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoRest {

	@Autowired
	private HealthMonitorConfig healthMonitorConfig;

	@GetMapping("/change/servers/status")
	public String changeServerStatus(Boolean status) {
		if(status != null) {
			healthMonitorConfig.setStatus(status);
		}
		return "success";
	}
}

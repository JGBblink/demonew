package com.example.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableApolloConfig
@RestController
public class ApolloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApolloApplication.class, args);
	}

	@Value("${jgbTest:JJJ}")
	String test;
	@Value("${jgb.apollo.test.name:null}")
	String testName;
	@Value("${jgb.apollo.test.address:null}")
	String address;

	@GetMapping("/test/apollo")
	public String getTestApollo() {

		return address;
	}
}

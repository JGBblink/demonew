package com.example.eureka;

import com.example.eureka.extend.MySpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

	@Autowired
	Environment environment;

	public static void main(String[] args) {
		SpringApplication application = new MySpringApplication(EurekaApplication.class);
		application.run(args);

	}

}

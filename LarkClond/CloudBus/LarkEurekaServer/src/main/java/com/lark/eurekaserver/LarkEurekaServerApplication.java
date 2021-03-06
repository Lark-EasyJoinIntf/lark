package com.lark.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LarkEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LarkEurekaServerApplication.class, args);
	}
}

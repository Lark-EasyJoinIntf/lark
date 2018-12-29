package com.lark.configclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@RefreshScope
public class LarkConfigClientApplication {
	/*http://localhost:8881/actuator/bus-refresh*/
	public static void main(String[] args) {
		SpringApplication.run(LarkConfigClientApplication.class, args);
	}
}

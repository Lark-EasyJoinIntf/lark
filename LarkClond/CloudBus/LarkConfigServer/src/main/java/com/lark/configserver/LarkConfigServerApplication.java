package com.lark.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * cmd 命令行下请求刷新配置更新：curl -X POST http://localhost:7002/actuator/bus-refresh
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
@EnableDiscoveryClient
@RefreshScope
public class LarkConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LarkConfigServerApplication.class, args);
	}
}

package com.lark.singledsweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.lark.**.dao")
@ComponentScan(basePackages={"com.lark.**.service","com.lark.**.controller","com.lark.**.security"})
public class SingledswebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SingledswebApplication.class, args);
	}

}

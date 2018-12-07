package com.lark.multydsweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 加上exclude = DataSourceAutoConfiguration.class解决以下启动问题：
 *   Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
 *   Consider the following:
 *		If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
 *		If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active)
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages="com.lark") //解决依赖工程中定义的组件无法实例化问题
public class MultydswebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultydswebApplication.class, args);
	}
}

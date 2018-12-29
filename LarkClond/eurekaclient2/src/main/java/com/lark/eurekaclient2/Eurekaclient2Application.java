package com.lark.eurekaclient2;

import com.entity.entities.Page;
import com.entity.entities.Result;
import com.entity.entities.Status;
import com.entity.entities.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableDiscoveryClient
public class Eurekaclient2Application {

	public static void main(String[] args) {
		SpringApplication.run(Eurekaclient2Application.class, args);
	}

	@Value("${server.port}")
	String port;

	@GetMapping("/hi")
	public Result<String> home(@RequestParam(value = "name", defaultValue = "forezp") String name) {
		return new Result(Status.SUCCESS, "hi " + name + " ,i am from port:" + port);
	}

	@PostMapping("/sethi")
	public Result<UserInfo> sethome(@RequestBody UserInfo user) {
		return new Result(Status.SUCCESS, user);
		//return "hi " + user.getName() + " ,post i am from port:" + port;
	}

	@PostMapping("/getForPage")
	public Result<Page<UserInfo>> getForPage(@RequestBody Page<UserInfo> page) {
		List<UserInfo> data = new ArrayList<>();
		data.add(new UserInfo("张三", "M"));
		data.add(new UserInfo("王五", "F"));
		page.setTotalCount(123);
		page.setData(data);
		Result<Page<UserInfo>> result = new Result(Status.SUCCESS, page);
		System.out.println("S1 status="+result.getStatus());
		return result;
	}
}

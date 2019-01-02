package com.lark.eurekaclient2;

import com.alibaba.fastjson.JSONObject;
import com.entity.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.entity.entities")
public class Eurekaclient2Application {

	public static void main(String[] args) {
		SpringApplication.run(Eurekaclient2Application.class, args);
	}

	@Autowired
	private LimitUtil limitUtil;

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

	@PostMapping("/login")
	public Result<String> login(@RequestBody UserInfo user) {
		//直接用的入参信息，获取用户及权限信息及后续的验证逻辑省略
		//......
		JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(user));
		jsonObject.remove("password");
		String jwt = limitUtil.setLoginInfo(user.getAccount(), jsonObject);
		return new Result(Status.SUCCESS, jwt);
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

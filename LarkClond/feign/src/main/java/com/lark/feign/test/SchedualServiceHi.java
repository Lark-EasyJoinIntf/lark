package com.lark.feign.test;

import com.entity.entities.Page;
import com.entity.entities.Result;
import com.entity.entities.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * feign作为服务消费者，定义一个feign接口，通过@ FeignClient（“服务名”），来指定调用哪个服务。
 * 比如在代码中调用了service-hi服务的“/hi”接口
 * fallback的配置为使用Feign的断路器（Hystrix），SchedualServiceHiHystric是SchedualServiceHi接口的实现
 */
@FeignClient(value = "service-hi",fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHi {
    @GetMapping(value = "/hi")
    Result<String> sayHiFromClientOne(@RequestParam(value = "name") String name);

    @PostMapping(value = "/sethi")
    Result<UserInfo> setHiFromClientOne(@RequestBody UserInfo user);

    @PostMapping(value = "/login")
    Result<String> login(@RequestBody UserInfo user);

    @PostMapping(value = "/getForPage")
    public Result<Page<UserInfo>> getForPage(@RequestBody Page<UserInfo> page);
}

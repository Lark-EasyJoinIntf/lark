package com.lark.feign.test;

import com.entity.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HiController {

    //编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错。
    @Autowired
    SchedualServiceHi schedualServiceHi;

    @GetMapping(value = "/hi")
    public Result<String> sayHi(@RequestParam String name) {
        return schedualServiceHi.sayHiFromClientOne( name );
    }

    @PostMapping(value = "/sethi")
    public Result<UserInfo> setHi(@RequestBody BaseParamEntity<UserInfo> user) {
        return schedualServiceHi.setHiFromClientOne( user.getParam() );
    }

    @PostMapping(value = "/vip/login")
    public Result<String> login(@RequestBody UserInfo user) {
        return schedualServiceHi.login( user);
    }

    @PostMapping(value = "/getUserForPage")
    public Result<Page<UserInfo>> getUserForPage(@RequestBody BaseParamEntity<Page> page) {
        Result<Page<UserInfo>> result = new Result(Status.EXC.setMessage("未提供分页参数"));
        if(page.getParam()!=null){
            result = schedualServiceHi.getForPage(page.getParam());
        }
        System.out.println("S2 status="+result.getStatus());
        return result;
    }
}

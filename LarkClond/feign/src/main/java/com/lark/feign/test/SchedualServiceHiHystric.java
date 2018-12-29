package com.lark.feign.test;

import com.entity.entities.Page;
import com.entity.entities.Result;
import com.entity.entities.Status;
import com.entity.entities.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi{
    @Override
    public Result<String> sayHiFromClientOne(String name) {
        return new Result(Status.FAILED.setMessage("Sorry "+name+",server is shutdown get!"));
    }

    @Override
    public Result<UserInfo> setHiFromClientOne(@RequestBody UserInfo user) {
        return new Result(Status.FAILED.setMessage("Sorry "+user.getName()+",server is shutdown post!"));
    }

    @Override
    public Result<Page<UserInfo>> getForPage(Page<UserInfo> page) {
        return new Result(Status.FAILED.setMessage("Sorry ,server is shutdown post!"));
    }
}

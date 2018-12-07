package com.lark.feign.test;

import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi{
    @Override
    public String sayHiFromClientOne(String name) {
        return "Sorry "+name+",server is shutdown!";
    }
}

package com.lark.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class HiController {

    @Value("${from}")
    private String from;

    @GetMapping(value = "/hi")
    public String hi(){
        return from;
    }
}

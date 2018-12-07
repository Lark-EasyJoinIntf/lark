package com.lark.comm.version;


import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;


/**
 *  使用
 *  @ApiVersion(1)
    @RequestMapping("{version}/dd")
    public class HelloController {

    }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    /**
     * 标识版本号
     * @return
     */
    int value();
}


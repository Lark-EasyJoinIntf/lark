package com.lark.authority.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data //自动生成 get、set 方法
@Slf4j //日志打印
//@ToString(of = {"id","username"})  //tostring() 方法里面只打印 id ,userName 字段
public class SysUser{
    private Integer id;
    private String username;
    private String password;
    private String sex;
    private String roleName;
}

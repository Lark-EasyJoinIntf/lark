package com.lark.authority.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data //自动生成 get、set 方法
@Slf4j //日志打印
public class Permission {

    private int id;
    //权限名称
    private String name;

    //权限描述
    private String descritpion;

    //授权链接
    private String url;

    //父节点id
    private int pid;

    //请求方法
    private String method;
}

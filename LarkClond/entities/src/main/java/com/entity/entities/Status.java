package com.entity.entities;

/**
 * 系统状态定义
 */
public enum Status {

    SUCCESS(0, "请求成功"),
    EXC(-1, "系统异常"),
    FAILED(-2, "请求失败"),
    LIMIT_TOKEN_MISS(-1001, "[token]参数缺失或为空"),
    LIMIT_USER_NOT_LOGIN(-1002, "用户未登录"),
    LIMIT_USER_LOST_ACCOUNT_ATTR(-1003, "登录信息缺失["+ AppPropUtil.get("lark.user.attr.account") +"]属性"),
    LIMIT_USER_INVALID(-1004, "登录失效");

    private int status;
    private String message;

    private Status(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Status setMessage(String message) {
        this.message = message;
        return this;
    }
}

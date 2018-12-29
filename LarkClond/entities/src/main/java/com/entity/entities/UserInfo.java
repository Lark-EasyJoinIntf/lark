package com.entity.entities;

public class UserInfo {

    private String name;
    private String sex;

    public UserInfo(){}

    public UserInfo(String name, String sex){
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

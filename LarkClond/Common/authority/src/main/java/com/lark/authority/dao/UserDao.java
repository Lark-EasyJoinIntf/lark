package com.lark.authority.dao;

import com.lark.authority.model.SysUser;

import java.util.List;

public interface UserDao {
    int insert(SysUser record);
    List<SysUser> selectUsers();
    SysUser getUser(Long id);
    SysUser getUserByName(String username);
}

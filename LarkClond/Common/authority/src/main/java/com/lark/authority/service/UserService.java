package com.lark.authority.service;

import com.github.pagehelper.PageInfo;
import com.lark.authority.model.SysUser;

public interface UserService {
    int addUser(SysUser user);
    PageInfo<SysUser> findAllUser(int pageNum, int pageSize);
    SysUser findUser(Long userId);
}

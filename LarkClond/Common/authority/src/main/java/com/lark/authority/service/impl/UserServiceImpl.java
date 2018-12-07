package com.lark.authority.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lark.authority.dao.UserDao;
import com.lark.authority.model.SysUser;
import com.lark.authority.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public int addUser(SysUser user) {
        return userDao.insert(user);
    }

    @Override
    public PageInfo<SysUser> findAllUser(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> user = userDao.selectUsers();
        PageInfo result = new PageInfo(user);
        return result;
    }

    @Override
    public SysUser findUser(Long userId) {
        return userDao.getUser(userId);
    }
}

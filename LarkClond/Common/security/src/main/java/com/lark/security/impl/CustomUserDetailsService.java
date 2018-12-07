package com.lark.security.impl;

import com.lark.authority.dao.PermissionDao;
import com.lark.authority.dao.UserDao;
import com.lark.authority.model.Permission;
import com.lark.authority.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public AuthorInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userDao.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<Permission> permissions = permissionDao.findByAdminUserId(user.getId());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Permission permission : permissions) {
            if (permission != null && permission.getName() != null) {
                GrantedAuthority grantedAuthority = new CustomGrantedAuthority(permission.getUrl(), permission.getMethod());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return new AuthorInfo(user, grantedAuthorities);
    }
}

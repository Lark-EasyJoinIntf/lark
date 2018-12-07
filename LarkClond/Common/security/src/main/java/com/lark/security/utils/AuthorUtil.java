package com.lark.security.utils;

import com.lark.security.impl.AuthorInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取登录用户信息工具类
 * @since 2018-11-12
 * @author Smile.li
 */
public class AuthorUtil {

    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static AuthorInfo getAuthor(){
        Object author = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if("anonymousUser".equals(author)){
            return null;
        }else{
            return (AuthorInfo)author;
        }
    }
}

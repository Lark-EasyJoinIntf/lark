package com.lark.singledsweb.controller;

import com.lark.authority.service.UserService;
import com.lark.security.impl.AuthorInfo;
import com.lark.security.utils.AuthorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

   @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @GetMapping("/welcome")
    ModelAndView welcome() {
        System.out.println("8081");
        return new ModelAndView("welcome");
    }
    /*@PostMapping("/login")
    public SysUser login(SysUser user, String error, String logout){
        if (user != null) {
            return user;
        }
        return null;
    }*/
    @GetMapping("/login")
    public ModelAndView login(String error, String logout, HttpServletRequest request){
        if(error != null || logout != null){
            return new ModelAndView("index");
        }
        AuthorInfo user = AuthorUtil.getAuthor();
        if(user == null){
            return new ModelAndView("index").addObject("nologin","未登录");
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            session.setAttribute("user", user);
            System.out.println("不存在session");
        } else {
            System.out.println("存在session");
        }
        redisTemplate.opsForValue().set("user"+user.getUsername(), user);
        return null;
    }

    @GetMapping("/logout")
    public ModelAndView logout(){
        return new ModelAndView("index");
    }
}

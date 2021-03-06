package com.lark.multydsweb.controller;

import com.lark.authority.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

   @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @GetMapping("/welcome")
    ModelAndView welcome() {
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
    public ModelAndView login(String error, String logout){
        if(error != null || logout != null){
            return new ModelAndView("index");
        }
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if("anonymousUser".equals(user)){
            return new ModelAndView("index").addObject("nologin","未登录");
        }
        return null;
    }

    @GetMapping("/logout")
    public ModelAndView logout(){
        return new ModelAndView("index");
    }
}

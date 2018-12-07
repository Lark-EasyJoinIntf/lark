package com.lark.singledsweb.controller;

import com.lark.authority.model.SysUser;
import com.lark.authority.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public int addUser(SysUser user){
        return userService.addUser(user);
    }

    @GetMapping("/userlist")
    public ModelAndView findAllUser(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize, Model model){
        model.addAttribute("title", "用户列表");
        model.addAttribute("userList", userService.findAllUser(pageNum,pageSize).getList());
        return new ModelAndView("user/list","userModel",model);
    }

    @GetMapping("/{id}")
    public ModelAndView findUser(@PathVariable("id") Long id, Model model){
        model.addAttribute("user",userService.findUser(id));
        model.addAttribute("title","查看用户");
        return new ModelAndView("user/view","userModel",model);
    }
}

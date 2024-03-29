package com.wanglei.myapiinterface.controller;


import com.wanglei.myapiclientsdk.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 名称 API
 */
@RestController
public class NameController {

    @GetMapping("/name")
    public String getName(String name){
        return "你的名字是" + name;
    }

    @PostMapping("/name/post")
    public String getNamePost(@RequestParam  String name){
        return "POST 你的名字是" + name;
    }

    @PostMapping("/name/user")
    public String getUserNamePost(@RequestBody User user, HttpServletRequest request){
        return "POST 用户名字是" + user.getUsername();
    }
}

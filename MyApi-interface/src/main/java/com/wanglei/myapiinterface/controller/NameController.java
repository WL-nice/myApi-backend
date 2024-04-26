package com.wanglei.myapiinterface.controller;


import com.wanglei.myapiclientsdk.model.AddNum;
import com.wanglei.myapiclientsdk.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 名称 API
 */
@Slf4j
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
        String source = request.getHeader("name");
        if(!"muqiu".equals(source)){
            return "非法请求";
        }
        return "POST 用户名字是: " + user.getUsername();
    }

    @PostMapping("/add/num")
    public String getAddNumPost(@RequestBody AddNum addNum, HttpServletRequest request){
        String source = request.getHeader("name");
        if(!"muqiu".equals(source)){
            return "非法请求";
        }
//        log.info(source);
        if(addNum == null || addNum.getNum1() == null || addNum.getNum2() == null){
            return "参数不能为空";
        }
        int sum = addNum.getNum1() + addNum.getNum2();
        return "两数之和是: " + sum;
    }
}

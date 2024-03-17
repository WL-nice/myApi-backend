package com.wanglei.myApi.service;

import com.wanglei.myApi.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userservice;

    @Test
    void testAdduser() {
        User user = new User();
        user.setUsername("test");
        user.setUserAccount("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("231");
        user.setPhone("23");
        user.setEmail("123");

        boolean result = userservice.save(user);
        user.getId();
        assertTrue(result);


    }

    @Test
    void userRegister() {
        String userAccount = "muqiu";
        String userPasward = "";
        String checkPassword = "12345678";
        String acptCode = "1212";
        long result = userservice.UserRegister(userAccount, userPasward, checkPassword);
        Assertions.assertEquals(-1, result);

        userPasward = "12345678";
        userAccount = "mu";
        result = userservice.UserRegister(userAccount, userPasward, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "muqiu";
        userPasward = "123456";
        result = userservice.UserRegister(userAccount, userPasward, checkPassword);
        Assertions.assertEquals(-1, result);


        userAccount = "mu qiu";
        result = userservice.UserRegister(userAccount, userPasward, checkPassword);
        Assertions.assertEquals(-1, result);

        userPasward = "12345678";
        checkPassword = "123456";
        result = userservice.UserRegister(userAccount, userPasward, checkPassword);
        Assertions.assertEquals(-1, result);

        checkPassword = "12345678";
        userAccount = "123";
        result = userservice.UserRegister(userAccount, userPasward, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "yuyu";
        userPasward = "12345678";
        checkPassword = "12345678";
        result = userservice.UserRegister(userAccount, userPasward, checkPassword);
        //Assertions.assertEquals(-1, result);


    }


}
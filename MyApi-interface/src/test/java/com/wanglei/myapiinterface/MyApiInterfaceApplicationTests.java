package com.wanglei.myapiinterface;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class MyApiInterfaceApplicationTests {

    @Test
    void contextLoads() {
        int number = new Random().nextInt(10) + 1;
        System.out.println(number);
    }

}

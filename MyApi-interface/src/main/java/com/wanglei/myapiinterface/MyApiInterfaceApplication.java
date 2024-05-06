package com.wanglei.myapiinterface;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wanglei.myapiinterface.mapper")
public class MyApiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApiInterfaceApplication.class, args);
    }

}

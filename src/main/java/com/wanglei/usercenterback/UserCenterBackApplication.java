package com.wanglei.usercenterback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wanglei.usercenterback.mapper")//扫描mapper
public class UserCenterBackApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserCenterBackApplication.class, args);
	}

}

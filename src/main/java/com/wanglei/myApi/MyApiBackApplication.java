package com.wanglei.myApi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wanglei.myApi.mapper")//扫描mapper
public class MyApiBackApplication {

	public static void main(String[] args) {

		SpringApplication.run(MyApiBackApplication.class, args);
	}

}

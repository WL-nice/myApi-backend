package com.wanglei.MyApi;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wanglei.MyApi.mapper") //扫描mapper
@EnableDubbo
public class MyApiBackApplication {

	public static void main(String[] args) {

		SpringApplication.run(MyApiBackApplication.class, args);
	}

}

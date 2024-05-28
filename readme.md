# MyAPI

> 一个丰富的API开放调用平台，为开发者提供便捷、实用的API调用体验
>
> 在线体验地址：[MyAPI](http://wlsite.icu)

管理员可以接入并发布接口；用户可以注册登录并开通接口调用权限、浏览接口、在线调试，还能在客户端SDK中调用接口

## 后端技术选型：

- Java Spring Boot框架
- MySQL 数据库
- MyBatis-Plus 及MyBatis X自动生成
- API签名认证（Http调用）
- Spring Boot Start ( SDK 开发）
- Dubbo 分布式 (RPC、Nacos）
- Spring Cloud Gateway 微服务网关
- Knife4j接口文档生成
- Hutool 、Gson等工具库

## 项目结构
![](https://cdn.nlark.com/yuque/0/2024/jpeg/42967483/1711793713758-d4d05ac8-96d8-4d78-9473-4831474395fe.jpeg)

## 项目模块

- MyApi-backend: 接口管理，主要包括用户、接口相关功能；
- MyApi-common：公共模块 ，公共实体、公共常量等；
- MyApi-gateway：网关模块， 鉴权、跨域等；
- MyApi-interface：模拟接口
- MyApi-client-sdk：提供给开发者的sdk
## 功能模块

- 用户：登录注册，个人信息修改，获取accessKey和secretkey
- 管理员：登录注册，个人信息修改，用户管理，接口管理
- 接口：浏览接口信息，签名校验（网关实现），调用接口，接口搜索




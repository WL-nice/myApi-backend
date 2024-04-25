package com.wanglei.MyApi.model.domain.request.user;

import lombok.Data;

@Data
public class UserAddRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;


}

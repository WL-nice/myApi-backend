package com.wanglei.MyApi.model.domain.request.user;

import lombok.Data;

@Data
public class UserUpdatePasswordRequest {

    Long id;
    String password;
    String checkPassword;
}

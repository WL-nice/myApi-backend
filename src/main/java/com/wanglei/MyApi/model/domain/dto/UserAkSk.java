package com.wanglei.MyApi.model.domain.dto;

import lombok.Data;

@Data
public class UserAkSk {
    private String accessKey;
    private String secretKey;

    public UserAkSk(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
    public UserAkSk() {
    };
}

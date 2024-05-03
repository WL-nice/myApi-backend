package com.wanglei.MyApi.model.domain.vo;

import lombok.Data;

/**
 * 验证码封装类
 */
@Data
public class ValidCodeVo {

    /**
     * 验证码key
     */
    String codeKey;

    /**
     * 验证码图片
     */
    String codeValue;
}

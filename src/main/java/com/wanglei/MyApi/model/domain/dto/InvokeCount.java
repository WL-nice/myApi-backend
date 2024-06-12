package com.wanglei.MyApi.model.domain.dto;

import lombok.Data;

@Data
public class InvokeCount {

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;
}

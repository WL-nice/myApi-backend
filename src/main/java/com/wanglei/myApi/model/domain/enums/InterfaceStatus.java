package com.wanglei.myApi.model.domain.enums;

/**
 * 接口状态枚举类
 */
public enum InterfaceStatus {

    online(1, "上线"),
    offline(0, "下线");


    private final int code;


    /**
     * 状态详情
     */
    private final String description;

    InterfaceStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }


    public String getDescription() {
        return description;
    }
}
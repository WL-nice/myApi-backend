package com.wanglei.MyApi.commmon;

/**
 * 错误码
 */
public enum ErrorCode {

    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40000,"请求数据为空",""),
    NO_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    SYSTEM_ERROR(50000,"系统内部异常","");




    private final int code;

    /**
     * 状态码信息
     */
    private final String messsge;

    /**
     * 状态码详情
     */
    private final String description;

    ErrorCode(int code, String messsge, String description) {
        this.code = code;
        this.messsge = messsge;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMesssge() {
        return messsge;
    }

    public String getDescription() {
        return description;
    }
}

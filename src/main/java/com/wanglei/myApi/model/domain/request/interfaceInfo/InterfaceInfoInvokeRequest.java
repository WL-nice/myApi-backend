package com.wanglei.myApi.model.domain.request.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoInvokeRequest implements Serializable {



    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userrequestParams;



    private static final long serialVersionUID = 1L;
}

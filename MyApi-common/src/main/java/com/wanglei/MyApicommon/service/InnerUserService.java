package com.wanglei.MyApicommon.service;


import com.wanglei.MyApicommon.model.User;


/**
 * 用户服务
* @author muqiu
*/
public interface InnerUserService  {


    /**
     * 查询是否已分配密钥
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}

package com.wanglei.MyApicommon.service;



import com.wanglei.MyApicommon.model.InterfaceInfo;
import com.wanglei.MyApicommon.model.UserInterfaceInfo;

/**
* @author admin
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-03-16 10:55:46
*/
public interface InnerUserInterfaceInfoService{


    /**
     * 接口调用统计
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCount(long userId,long interfaceInfoId);
}

package com.wanglei.MyApicommon.service;


import com.wanglei.MyApicommon.model.InterfaceInfo;


/**
* @author admin
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-03-16 10:55:25
*/
public interface InnerInterfaceInfoService  {

     InterfaceInfo getInterfaceInfo(String path, String method);

}

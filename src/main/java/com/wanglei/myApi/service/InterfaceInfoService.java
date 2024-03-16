package com.wanglei.myApi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanglei.myApi.model.domain.InterfaceInfo;

/**
* @author admin
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-03-16 10:55:25
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

     void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

}

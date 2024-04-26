package com.wanglei.MyApi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglei.MyApicommon.model.UserInterfaceInfo;

import java.util.List;

/**
* @author admin
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-03-16 10:55:46
* @Entity com.wanglei.usercenterback.model.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}





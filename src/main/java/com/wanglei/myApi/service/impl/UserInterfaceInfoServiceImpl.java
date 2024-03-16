package com.wanglei.myApi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglei.myApi.service.UserInterfaceInfoService;
import com.wanglei.myApi.model.domain.UserInterfaceInfo;
import com.wanglei.myApi.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-03-16 10:55:46
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

}





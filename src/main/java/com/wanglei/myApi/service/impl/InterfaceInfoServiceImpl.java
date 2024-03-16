package com.wanglei.myApi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglei.myApi.commmon.ErrorCode;
import com.wanglei.myApi.exception.BusinessException;
import com.wanglei.myApi.service.InterfaceInfoService;
import com.wanglei.myApi.model.domain.InterfaceInfo;
import com.wanglei.myApi.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-03-16 10:55:25
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add && (StringUtils.isAnyBlank(name))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

}






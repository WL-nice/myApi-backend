package com.wanglei.myApi.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglei.myApi.commmon.ErrorCode;
import com.wanglei.myApi.exception.BusinessException;
import com.wanglei.myApi.service.UserInterfaceInfoService;
import com.wanglei.myApi.model.domain.UserInterfaceInfo;
import com.wanglei.myApi.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author admin
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2024-03-16 10:55:46
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if (add && (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");

        }
        if (leftNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "调用次数不能小于0");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean invokeCount(long userId, long interfaceInfoId) {
        UpdateWrapper<UserInterfaceInfo> updateQueryWrapper = new UpdateWrapper<>();
        updateQueryWrapper.eq("userId",userId);
        updateQueryWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateQueryWrapper.gt("leftNum",0);
        updateQueryWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateQueryWrapper);
    }
}





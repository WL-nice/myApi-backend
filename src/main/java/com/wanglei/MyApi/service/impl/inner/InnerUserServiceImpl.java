package com.wanglei.MyApi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanglei.MyApi.commmon.ErrorCode;
import com.wanglei.MyApi.exception.BusinessException;
import com.wanglei.MyApi.mapper.UserMapper;
import com.wanglei.MyApicommon.model.User;
import com.wanglei.MyApicommon.service.InnerUserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    UserMapper userMapper;
    @Override
    public User getInvokeUser(String accessKey) {
        if(StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}

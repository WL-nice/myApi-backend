package com.wanglei.MyApi.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglei.MyApi.commmon.ErrorCode;
import com.wanglei.MyApi.constant.CommonConstant;
import com.wanglei.MyApi.exception.BusinessException;
import com.wanglei.MyApi.mapper.InterfaceInfoMapper;
import com.wanglei.MyApi.model.domain.enums.InterfaceStatus;
import com.wanglei.MyApi.model.domain.request.interfaceInfo.InterfaceInfoInvokeRequest;
import com.wanglei.MyApi.model.domain.request.interfaceInfo.InterfaceInfoQueryRequest;
import com.wanglei.MyApi.service.InterfaceInfoService;
import com.wanglei.MyApi.service.UserService;
import com.wanglei.MyApicommon.model.InterfaceInfo;
import com.wanglei.MyApicommon.model.User;
import com.wanglei.myapiclientsdk.utils.SignUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-03-16 10:55:25
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    UserService userService;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestParams = interfaceInfo.getRequestParams();
        String method = interfaceInfo.getMethod();


        // 创建时，所有参数必须非空
        if (add && (StringUtils.isAnyBlank(name))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        if (StringUtils.isBlank(description)
                || StringUtils.isBlank(url)
                || StringUtils.isBlank(method)
        ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String sortField = interfaceInfoQueryRequest.getSortField();

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.eq(userId != null && userId > 0, "userId", userId);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public String invokeInterfaceInfo(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        // 根据请求信息获取接口信息
        Long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        // 接口信息存在性校验
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "未发现接口");
        }
        if (oldInterfaceInfo.getStatus().equals(InterfaceStatus.offline.getCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已下线");
        }
        User usert = userService.getById(loginUser.getId());
        String accessKey = usert.getAccessKey();
        String secretKey = usert.getSecretKey();
        String method = oldInterfaceInfo.getMethod();
        String url = oldInterfaceInfo.getUrl();
        if (method.equals("POST")) {
            try (cn.hutool.http.HttpResponse httpResponse = HttpRequest.post(url)
                    .addHeaders(getHeaderMap(userRequestParams, accessKey, secretKey))
                    .body(userRequestParams)
                    .execute()) {
                return httpResponse.body();
            }
        } else if (method.equals("GET")) {
            try (cn.hutool.http.HttpResponse httpResponse = HttpRequest.get(url)
                    .addHeaders(getHeaderMap(userRequestParams, accessKey, secretKey))
                    .execute()) {
                return httpResponse.body();
            }
        }
        return null;
    }

    @Override
    public boolean updateStatus(Long id, int status) {
        //判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "未发现接口");
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(status);
        return this.updateById(interfaceInfo);
    }

    private Map<String, String> getHeaderMap(String body, String accessKey, String secretKet) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("nonce", RandomUtil.randomNumbers(5));
        hashMap.put("body", body);
        hashMap.put("accessKey", accessKey);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(accessKey, secretKet));
        hashMap.put("name", "muqiu");
        return hashMap;
    }

}






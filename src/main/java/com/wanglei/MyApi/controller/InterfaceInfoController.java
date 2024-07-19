package com.wanglei.MyApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglei.MyApi.annotation.AuthCheck;
import com.wanglei.MyApi.commmon.*;
import com.wanglei.MyApi.constant.CrossSite;
import com.wanglei.MyApi.exception.BusinessException;
import com.wanglei.MyApi.model.domain.enums.InterfaceStatus;
import com.wanglei.MyApi.model.domain.request.interfaceInfo.InterfaceInfoAddRequest;
import com.wanglei.MyApi.model.domain.request.interfaceInfo.InterfaceInfoInvokeRequest;
import com.wanglei.MyApi.model.domain.request.interfaceInfo.InterfaceInfoQueryRequest;
import com.wanglei.MyApi.model.domain.request.interfaceInfo.InterfaceInfoUpdateRequest;
import com.wanglei.MyApi.model.domain.vo.InterfaceInfoVO;
import com.wanglei.MyApi.service.InterfaceInfoService;
import com.wanglei.MyApi.service.UserInterfaceInfoService;
import com.wanglei.MyApi.service.UserService;
import com.wanglei.MyApicommon.model.InterfaceInfo;
import com.wanglei.MyApicommon.model.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

@RestController
@RequestMapping("/InterfaceInfo")
@Slf4j
@CrossOrigin(origins = {"http://localhost:8000", "http://192.168.237.129", CrossSite.ALLOW_ORIGIN}, allowCredentials = "true")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "未发现接口");
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get/vo")
    @AuthCheck
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfoVO interfaceInfoVO = interfaceInfoService.getInterfaceInfoVOById(id);
        return ResultUtils.success(interfaceInfoVO);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(
            @RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfo);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 发布
     */

    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();

        boolean result = interfaceInfoService.updateStatus(id, InterfaceStatus.online.getCode());

        return ResultUtils.success(result);
    }

    /**
     * 下线接口
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //判断是否存在
        Long id = idRequest.getId();

        boolean result = interfaceInfoService.updateStatus(id, InterfaceStatus.offline.getCode());

        return ResultUtils.success(result);
    }

    /**
     * 测试调用
     */
    @PostMapping("/invoke")
    @AuthCheck
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        // 校验请求参数
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String result = interfaceInfoService.invokeInterfaceInfo(interfaceInfoInvokeRequest, request);
        return ResultUtils.success(Objects.requireNonNullElse(result, "调用失败"));
    }

    @GetMapping("/sdk")
    public void getSdk(HttpServletResponse response) throws IOException {
        // 获取要下载的文件
        org.springframework.core.io.Resource resource = new ClassPathResource("MyApi-client-sdk-0.0.1.jar");
        InputStream inputStream = resource.getInputStream();

        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=MyApi-client-sdk-0.0.1.jar");

        // 将文件内容写入响应
        try (OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }


}

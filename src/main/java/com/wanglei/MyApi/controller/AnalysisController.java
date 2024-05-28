package com.wanglei.MyApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanglei.MyApi.commmon.BaseResponse;
import com.wanglei.MyApi.constant.CrossSite;
import com.wanglei.MyApi.commmon.ResultUtils;
import com.wanglei.MyApi.mapper.UserInterfaceInfoMapper;
import com.wanglei.MyApi.model.domain.vo.InterfaceInfoVO;
import com.wanglei.MyApi.service.InterfaceInfoService;
import com.wanglei.MyApicommon.model.InterfaceInfo;
import com.wanglei.MyApicommon.model.UserInterfaceInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
@CrossOrigin(origins = {"http://localhost:8000","http://192.168.237.129", CrossSite.ALLOW_ORIGIN},allowCredentials = "true")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 获取top3接口信息
     * @return
     */
    @GetMapping("/top/interface")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo(){
        // 从数据库获取调用次数排名前3的接口信息
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);

        // 根据接口信息ID进行分组，以便后续处理
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdMap = userInterfaceInfos.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        // 获取接口信息ID列表
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",interfaceInfoIdMap.keySet());
        List<InterfaceInfo> userInterfaceInfoList = interfaceInfoService.list(queryWrapper);

        // 封装VO
        List<InterfaceInfoVO> interfaceInfoVOList = userInterfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            interfaceInfoVO.setTotalNum(interfaceInfoIdMap.get(interfaceInfo.getId()).get(0).getTotalNum());
            return interfaceInfoVO;
        }).toList();
        return ResultUtils.success(interfaceInfoVOList);
    }

}

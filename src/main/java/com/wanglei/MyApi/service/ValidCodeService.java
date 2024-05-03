package com.wanglei.MyApi.service;

import com.wanglei.MyApi.model.domain.vo.ValidCodeVo;
import org.springframework.stereotype.Service;


public interface ValidCodeService {
    /**
     * 生成图片验证码
     * @return
     */
    ValidCodeVo getValidCode();
}

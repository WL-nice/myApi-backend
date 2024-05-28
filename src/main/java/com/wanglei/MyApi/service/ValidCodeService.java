package com.wanglei.MyApi.service;

import com.wanglei.MyApi.model.domain.vo.ValidCodeVO;


public interface ValidCodeService {
    /**
     * 生成图片验证码
     * @return
     */
    ValidCodeVO getValidCode();
}

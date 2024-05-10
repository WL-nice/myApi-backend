package com.wanglei.MyApi.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.wanglei.MyApi.model.domain.vo.ValidCodeVo;
import com.wanglei.MyApi.service.ValidCodeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ValidCodeServiceImpl implements ValidCodeService {

    @Resource
    RedisTemplate<String ,String> redisTemplate;

    /**
     * 生成图片验证码
     * @return
     */
    @Override
    public ValidCodeVo getValidCode() {
        //生成验证码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(100, 50, 4, 10);
        String codeValue = circleCaptcha.getCode();
        String imageBase64 = circleCaptcha.getImageBase64();

        //存入redis
        String key = "user:captcha:"+System.currentTimeMillis();
        redisTemplate.opsForValue().set(key,codeValue,1, TimeUnit.MINUTES);

        //返回
        ValidCodeVo validCodeVo = new ValidCodeVo();
        validCodeVo.setCodeKey(key);
        validCodeVo.setCodeValue("data:image/png;base64,"+imageBase64);
        return validCodeVo;
    }
}

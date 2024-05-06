package com.wanglei.myapiinterface.controller;

import com.wanglei.myapiinterface.model.LoveWord;
import com.wanglei.myapiinterface.service.LoveWordService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController

public class LoveWordController {
    @Resource
    private LoveWordService loveWordService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/loveWord")
    public String getLoveWord() {
        Long count = 0L;
        String key = "interface:wordsCount";

        count = (Long) redisTemplate.opsForValue().get(key);
        if (count == null) {
            count = loveWordService.count();
            redisTemplate.opsForValue().set(key, count, 1, TimeUnit.HOURS);
        }

        Long number = new Random().nextLong(count) + 1;
        String wordsKey = "interface:words:" + number;
        String result = (String) redisTemplate.opsForValue().get(wordsKey);
        if(result == null){
            result = loveWordService.getById(number).getWord();
            redisTemplate.opsForValue().set(wordsKey, result, 1, TimeUnit.HOURS);
        }
        return result;
    }
}

package com.wanglei.myapiinterface.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglei.myapiinterface.model.LoveWord;
import com.wanglei.myapiinterface.service.LoveWordService;
import com.wanglei.myapiinterface.mapper.LoveWordMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【love_word】的数据库操作Service实现
* @createDate 2024-05-06 14:24:55
*/
@Service
public class LoveWordServiceImpl extends ServiceImpl<LoveWordMapper, LoveWord>
    implements LoveWordService{

}





package com.wanglei.MyApi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglei.MyApicommon.model.User;
import org.apache.ibatis.annotations.Mapper;


/**
* @author master
* @description 针对表【user(用户)】的数据库操作Mapper
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}





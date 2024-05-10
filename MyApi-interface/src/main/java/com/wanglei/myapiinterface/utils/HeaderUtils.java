package com.wanglei.myapiinterface.utils;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtils {

    public static boolean isHaveHeader(HttpServletRequest request){
        return "muqiu".equals(request.getHeader("source"));
    }
}

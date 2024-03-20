package com.wanglei.myapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wanglei.myapiclientsdk.model.User;
import com.wanglei.myapiclientsdk.utils.SignUtils;


import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 */
public class MyApiClient {
    private String accessKey;
    private String secretKet;

    private static final String GATEWAY_HOST = "http://localhost:8090";//网关端口

    public MyApiClient(String accessKey, String secretKet) {
        this.accessKey = accessKey;
        this.secretKet = secretKet;
    }

    public String getName(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        return HttpUtil.get(GATEWAY_HOST + "/api/name", paramMap);
    }

    public String getPostName(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        return HttpUtil.post(GATEWAY_HOST + "/api/name", paramMap);
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("nonce", RandomUtil.randomNumbers(5));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(body, secretKet));
        return hashMap;
    }

    public String getUserName(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();


        return httpResponse.body();
    }


}

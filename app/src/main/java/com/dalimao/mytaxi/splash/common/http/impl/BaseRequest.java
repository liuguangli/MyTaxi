package com.dalimao.mytaxi.splash.common.http.impl;

import com.dalimao.mytaxi.splash.common.http.IRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinny on 2018/2/10.
 * 封装参数的实现
 */

public class BaseRequest implements IRequest {

    private String method = POST;
    private String              url;
    private Map<String, String> header;
    private Map<String, Object> body;

    public BaseRequest() {

    }

    public BaseRequest(String url) {
        /**
         * 公共参数及头部信息
         */
        this.url = url;
        header = new HashMap<>();
        body = new HashMap<>();
        header.put("Application-Id", "myTaxiID");
        header.put("API-Key", "myTaxiKey");
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setHeader(String key, String value) {
        header.put(key, value);
    }

    @Override
    public void setBody(String key, String value) {
        body.put(key, value);
    }

    @Override
    public String getUrl() {
        if (GET.equals(method)) {
            /**组装get请求参数*/
            for (String key : body.keySet()) {
                url = url.replace("${" + key + "}", body.get(key).toString());
            }
        }
        return url;
    }

    @Override
    public Map<String, String> getHeader() {
        return header;
    }

    @Override
    public Object getBody() {
        if (body != null) {
            /**组装Post方法请求参数*/
            return new Gson().toJson(this.body, HashMap.class);
        } else {
            return "{}";
        }
    }
}

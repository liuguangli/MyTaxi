package yx.taxi.common.http.impl;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import yx.taxi.common.api.API;
import yx.taxi.common.http.IRequest;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class RequestImpl implements IRequest {
    private String url;
    private String method;
    private Map<String, String> header;
    private Map<String, Object> body;
    public RequestImpl(String url) {
        this.url = url;
        header = new HashMap();
        body = new HashMap<>();
        header.put("X-Bmob-Application-Id", API.Config.getAppId());
        header.put("X-Bmob-REST-API-Key", API.Config.getAppKey());
    }
    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setHeader(String key, String value) {
        header.put(key,value);
    }

    @Override
    public void setBody(String key, String value) {
        body.put(key,value);
    }


    @Override
    public String getUrl() {
        if (GET.equals(method)) {
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
            return new Gson().toJson(this.body, HashMap.class);
        } else {
            return  "{}";
        }
    }
}

package yx.taxi.common.http;

import java.util.Map;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public interface IRequest {
    public static final String POST = "POST";
    public static final String GET = "GET";
    void setMethod(String method);
    void setHeader(String key, String value);
    void setBody(String key, String value);
    String getUrl();
    Map<String,String> getHeader();
    Object getBody();
}

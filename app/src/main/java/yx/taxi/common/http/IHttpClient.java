package yx.taxi.common.http;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public interface IHttpClient {
    IResponse get(IRequest request,boolean isForceCache);
    IResponse post(IRequest request,boolean isForceCache);
}

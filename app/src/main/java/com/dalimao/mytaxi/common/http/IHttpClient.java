package com.dalimao.mytaxi.common.http;

/**
 * HttpClient 抽象接口
 * Created by liuguangli on 17/4/24.
 */

public interface IHttpClient {
    IResponse get(IRequest request, boolean forceCache);
    IResponse post(IRequest request, boolean forceCache);
}

package com.dalimao.mytaxi.common.http;

/**
 * Created by jinny on 2018/2/10.
 * HttpClient抽象接口
 */

public interface IHttpClient {

    IResponse get(IRequest request, boolean forceCache);

    IResponse post(IRequest request, boolean forceCache);
}

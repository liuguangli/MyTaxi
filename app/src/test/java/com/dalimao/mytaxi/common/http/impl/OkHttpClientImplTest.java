package com.dalimao.mytaxi.common.http.impl;

import com.dalimao.mytaxi.common.http.IHttpClient;
import com.dalimao.mytaxi.common.http.IRequest;
import com.dalimao.mytaxi.common.http.IResponse;
import com.dalimao.mytaxi.common.http.api.API;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by jinny on 2018/2/12.
 */
public class OkHttpClientImplTest {

    IHttpClient httpClient;

    @Before
    public void setUp() throws Exception {
        httpClient = new OkHttpClientImpl();
        API.Config.setDeBug(false);
    }

    @Test
    public void get() throws Exception {
        //Request对象
        String url = API.Config.getDomain() + API.TEST_GET;
        IRequest request = new BaseRequest(url);
        request.setMethod(IRequest.GET);
        request.setBody("uid", "12345");
        request.setHeader("testHeader", "test header");
        IResponse response = httpClient.get(request, false);
        System.out.println("stateCode = " + response.getCode());
        System.out.println("body = " + response.getData());
    }

    @Test
    public void post() throws Exception {
        String url = API.Config.getDomain() + API.TEST_POST;
        IRequest request = new BaseRequest(url);
        request.setMethod(IRequest.GET);
        request.setBody("uid", "12345");
        request.setHeader("testHeader", "test header");
        IResponse response = httpClient.post(request, false);
        System.out.println("stateCode = " + response.getCode());
        System.out.println("body = " + response.getData());
    }
}
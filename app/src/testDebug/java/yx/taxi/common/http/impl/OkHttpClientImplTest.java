package yx.taxi.common.http.impl;

import org.junit.Before;
import org.junit.Test;

import yx.taxi.common.api.API;
import yx.taxi.common.http.IHttpClient;
import yx.taxi.common.http.IRequest;
import yx.taxi.common.http.IResponse;

/**
 * Created by yangxiong on 2018/4/26/026.
 */
public class OkHttpClientImplTest {


    private IHttpClient mOkHttpClient;

    @Before
    public void setUp(){
        mOkHttpClient = new OkHttpClientImpl( );
        API.Config.setDebug(true);
    }
    @Test
    public void get() throws Exception {
        String url = API.Config.getDomain() + API.TEST_GET;;
        IRequest request = new RequestImpl(url);
        request.setHeader("testHeader", "test header");
        request.setBody("uid", "123456");
        IResponse iResponse = mOkHttpClient.get(request, false);
        int code = iResponse.getCode( );
        String data = iResponse.getData( );
        System.out.println("code : " + code );
        System.out.println("data : " + data );
    }

    @Test
    public void post() throws Exception {
        String url = API.Config.getDomain() + API.TEST_POST;;
        IRequest request = new RequestImpl(url);
        request.setHeader("testHeader", "test header");
        request.setBody("uid", "123456");
        IResponse iResponse = mOkHttpClient.post(request,false);
        int code = iResponse.getCode( );
        String data = iResponse.getData( );
        System.out.println("code : " + code );
        System.out.println("data : " + data );
    }

}
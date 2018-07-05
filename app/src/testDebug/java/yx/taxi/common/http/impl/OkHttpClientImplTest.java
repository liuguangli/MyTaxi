package yx.taxi.common.http.impl;

import com.google.gson.Gson;

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
        Gson gson = new Gson();
        String jsonString = "{\n" +
                "  \"msg\": \"登录成功0885162973\", \n" +
                "  \"code\": 200, \n" +
                "  \"data\": {\n" +
                "    \"uid\": \"sfsdfsjkwejasdf3242342\", \n" +
                "    \"expired\": 1493626365811, \n" +
                "    \"account\": \"15919496912\", \n" +
                "    \"token\": \"12331493539965811\"\n" +
                "  }\n" +
                "}";
        BaseBizResponse bizRes =
                new Gson().fromJson(jsonString, BaseBizResponse.class);
        Account data = bizRes.getData( );
        System.out.println("setUp: "+data.getAccount());
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
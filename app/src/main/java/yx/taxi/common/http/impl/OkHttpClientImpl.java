package yx.taxi.common.http.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yx.taxi.common.http.IHttpClient;
import yx.taxi.common.http.IRequest;
import yx.taxi.common.http.IResponse;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class OkHttpClientImpl implements IHttpClient {
    OkHttpClient mOkHttpClient = new OkHttpClient.Builder().build();
    @Override
    public IResponse get(IRequest request, boolean isForceCache) {
        request.setMethod(IRequest.GET);
        Map<String, String> header = request.getHeader( );
        Set<String> keySet = header.keySet( );
        Request.Builder requestBuilder = new Request.Builder();
        for (String s : keySet) {
            requestBuilder.header(s,header.get(s));//添加header
        }
        String url = request.getUrl( );
        requestBuilder.url(url).get();//添加url
        Request requestOK = requestBuilder.build( );
        return excute(requestOK);
    }

    @Override
    public IResponse post(IRequest request, boolean isForceCache) {
        request.setMethod(IRequest.POST);
        Request.Builder builder = new Request.Builder();
        Map<String, String> header = request.getHeader( );
        Set<String> keySet = header.keySet( );
        for (String s : keySet) {
            builder.header(s,header.get(s));//添加header
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, request.getBody().toString());
        Request build = builder.url(request.getUrl()).post(body).build( );//添加body
        return excute(build);
    }


    private IResponse excute(Request requestOK) {
        ResponseImpl responseImpl =  new ResponseImpl();
        try {
            Response response = mOkHttpClient.newCall(requestOK).execute( );
            responseImpl.setCode(response.code());
            responseImpl.setData(response.body().string());
        } catch (IOException e) {
            e.printStackTrace( );
            responseImpl.setCode(responseImpl.STATE_UNKNOWN_ERROR);
            responseImpl.setData(e.getMessage());
        }
        return responseImpl;
    }

}

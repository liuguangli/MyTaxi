package com.dalimao.mytaxi;

/**
 * Created by dawei on 2018/6/17.
 */


import com.dalimao.mytaxi.http.IHttpClient;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpTest {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Test
    public void testGet() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
    @Test
    public void testPost() throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, "{\"name\":\"sunshine\"}");
        Request request = new Request.Builder()
                .url("http://httpbin.org/post")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
    @Test
    public void testInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long startTime = System.currentTimeMillis();
                Request request = chain.request();
                Response response = chain.proceed(request);
                long endTime = System.currentTimeMillis();
                System.out.println("cost time is " + (endTime - startTime));
                return response;
            }
        };
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Request request = new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testCache() {

        Cache cache = new Cache(new File("cache.cache"),1024*1024);

        OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();
        Request request = new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            Response responseCache = response.cacheResponse();
            Response responseNet = response.networkResponse();
            System.out.println(response.body().string());
            if (responseCache != null )
                System.out.println("responseCache " + responseCache);
            if (responseNet != null)
                System.out.println("responseNet " + responseNet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Created by dawei on 2018/6/17.
     */

    public static class OKHttpImplTest {
        IHttpClient mHttpClient;
        @Before
        public void setUp() {

        }
    }
}

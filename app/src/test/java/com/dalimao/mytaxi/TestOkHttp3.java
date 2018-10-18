package com.dalimao.mytaxi;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jinny on 2018/2/7.
 */

public class TestOkHttp3 {

    /**测试OkHttp Get方法*/
    @Test
    public void testGet(){
        /**1、创建OkHttpClient对象*/
        OkHttpClient client = new OkHttpClient();
        /**2、创建一个Request对象*/
        Request request = new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                .build();

        /**3、OKHttp去执行Request对象*/
        try {
            Response response = client.newCall(request).execute();
            //打印请求结果
            System.out.println("response: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**测试OkHttp Post方法*/
    @Test
    public void testPost(){
        //1、创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //2、创建一个Request对象
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType,"{\"name\":\"hello\"}");
        Request request = new Request.Builder()
                .url("http://httpbin.org/post")  //Http的请求行
                //.head()    //Http请求头
                .post(body)  //Http请求体
                .build();

        //OKHttp去执行Request对象
        try {
            Response response = client.newCall(request).execute();
            System.out.println("response: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试拦截器
     */
    @Test
    public void testInterceptor() {

        /**定义拦截器*/
        Interceptor interceptor = new Interceptor(){

            @Override
            public Response intercept(Chain chain) throws IOException {
                /**定义一个起始的时间*/
                long start = System.currentTimeMillis();

                /**通过request()方法截取到请求*/
                Request request = chain.request();
                /**把request传递给chain进行处理*/
                Response response = chain.proceed(request);

                /**定义一个结束的时间*/
                long end = System.currentTimeMillis();

                System.out.println("interceptor : cost time = " + (end - start));

                /**将response返回*/
                return response;
            }
        };

        /**1、创建OkHttpClient对象*/
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)  //添加拦截器
                .build();
        /**2、创建一个Request对象*/
        Request request = new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                .build();

        /**3、OKHttp去执行Request对象*/
        try {
            Response response = client.newCall(request).execute();
            System.out.println("response: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试缓存
     */
    @Test
    public void testCache(){

        /**
         * 创建一个缓存对象
         */
        Cache cache = new Cache(new File("cache.cache"), 1024 * 1024);

        /**1、创建OkHttpClient对象*/
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        /**2、创建一个Request对象*/
        Request request = new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                /**请求时，强制从缓存里面取。FORCE_NETWORK：从网络里取；FORCE_CACHE：从缓存里取。*/
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();

        /**3、OKHttp去执行Request对象*/
        try {
            Response response = client.newCall(request).execute();
            /**
             * Response提供了两种缓存：一种是从缓存中取的Response即cacheResponse;
             *                      一种是从网络里取的Response即netResponse。
             */
            Response responseCache = response.cacheResponse();
            Response responseNet = response.networkResponse();

            if (responseCache != null) {
                //从缓存响应
                System.out.println("response from cache");
            }

            if (responseNet != null) {
                //从网络响应
                System.out.println("response from net");
            }

            System.out.println("response: " + response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.dalimao.mytaxi.splash.common.http.impl;

import com.dalimao.mytaxi.splash.common.http.IResponse;

/**
 * Created by jinny on 2018/2/10.
 */

public class BaseResponse implements IResponse {

    public static final int STATE_UNKNOWN_ERROR = 100001;
    //状态码
    private int code;
    //响应数据
    private String data;

    @Override
    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }
}

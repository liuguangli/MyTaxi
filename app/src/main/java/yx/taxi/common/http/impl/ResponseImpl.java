package yx.taxi.common.http.impl;

import yx.taxi.common.http.IResponse;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class ResponseImpl implements IResponse {
    public static final int STATE_UNKNOWN_ERROR = 100001;
    public static final int STATE_OK = 200;
    private int code;
    private String data;
    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }
}

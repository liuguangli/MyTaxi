package yx.taxi.common.http.impl;

/**
 * Created by yangxiong on 2018/4/29/029.
 */

public class BaseBizResponse {
    private int code;
    private String msg;
    private Account data;

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

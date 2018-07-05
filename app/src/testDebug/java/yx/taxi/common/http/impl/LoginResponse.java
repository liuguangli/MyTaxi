package yx.taxi.common.http.impl;


/**
 * Created by yangxiong on 2018/4/29/029.
 */

public class LoginResponse extends BaseBizResponse {
    Account data;

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }
}

package yx.taxi.account.response;

import yx.taxi.common.http.biz.BaseBizResponse;

/**
 * Created by yangxiong on 2018/5/3/003.
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

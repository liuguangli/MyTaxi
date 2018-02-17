package com.dalimao.mytaxi.account.response;

import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;

/**
 * Created by jinny on 2018/2/17.
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

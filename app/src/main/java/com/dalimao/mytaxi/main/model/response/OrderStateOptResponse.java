package com.dalimao.mytaxi.main.model.response;

import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;

/**
 * Created by liuguangli on 17/6/3.
 * 订单操作状态
 */

public class OrderStateOptResponse extends BaseBizResponse {
    public final static int ORDER_STATE_CREATE = 0;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

package com.dalimao.mytaxi.main.model.response;

import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;
import com.dalimao.mytaxi.main.model.bean.Order;

/**
 * Created by liuguangli on 17/6/3.
 * 订单操作状态
 */

public class OrderStateOptResponse extends BaseBizResponse {
    public final static int ORDER_STATE_CREATE = 0;
    // 取消订单
    public static final int ORDER_STATE_CANCEL = -1;
    //  司机接单
    public static final int ORDER_STATE_ACCEPT = 1;
    private int state;
    // 携带操作的订单
    private Order data;
    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}










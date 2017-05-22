package com.dalimao.mytaxi.common.databus;

/**
 * Created by liuguangli on 17/5/17.
 * 数据订阅者，Presenter 要实现这个接口来接收数据
 */
public interface DataBusSubscriber {

    void onEvent(Object data);
}

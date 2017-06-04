package com.dalimao.mytaxi.main.view;

import com.dalimao.mytaxi.account.view.IView;
import com.dalimao.mytaxi.common.lbs.LocationInfo;
import com.dalimao.mytaxi.main.model.bean.Order;

import java.util.List;

/**
 * Created by liuguangli on 17/5/14.
 */

public interface IMainView extends IView {
    void showLoginSuc();

    /**
     * 附近司机
     * @param data
     */
    void showNears(List<LocationInfo> data);


    /**
     * 显示位置变化
     * @param locationInfo
     */
    void showLocationChange(LocationInfo locationInfo);

    /**
     *  显示呼叫成功发出
     */
    void showCallDriverSuc();

    /**
     *  显示呼叫未成功发出
     */
    void showCallDriverFail();




    /**
     * 取消订单成功
     */
    void showCancelSuc();
    /**
     *  显示取消定失败
     *
     */
    void showCancelFail();


    /**
     * 显示司机接单
     * @param mCurrentOrder
     */
    void showDriverAcceptOrder(Order mCurrentOrder);
}

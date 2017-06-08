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

    /**
     * 司机到达上车地点
     * @param mCurrentOrder
     */
    void showDriverArriveStart(Order mCurrentOrder);

    /**
     * 更新司机到上车点的路径
     * @param locationInfo
     */
    void updateDriver2StartRoute(LocationInfo locationInfo, Order order);

    /**
     * 更新司机到上车点的路径
     * @param order
     */
    void showStartDrive(Order order);

    /**
     *   显示到达终点
     * @param order
     */
    void showArriveEnd(Order order);

    /**
     *  更新司机到终点的路径
     * @param locationInfo
     */
    void updateDriver2EndRoute(LocationInfo locationInfo, Order order);


    /**
     * 支付成功
     * @param mCurrentOrder
     */
    void showPaySuc(Order mCurrentOrder);

    /**
     * 显示支付失败
     */
    void showPayFail();
}

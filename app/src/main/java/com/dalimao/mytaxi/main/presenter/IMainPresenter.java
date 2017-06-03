package com.dalimao.mytaxi.main.presenter;

import com.dalimao.mytaxi.common.lbs.LocationInfo;

/**
 * Created by liuguangli on 17/5/14.
 */

public interface IMainPresenter {
    void loginByToken();

    /**
     * 获取附近司机
     * @param latitude
     * @param longitude
     */
    void fetchNearDrivers(double latitude, double longitude);


    /**
     * 上报当前位置
     * @param locationInfo
     */
    void updateLocationToServer(LocationInfo locationInfo);
}

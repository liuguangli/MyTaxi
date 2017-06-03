package com.dalimao.mytaxi.main.model;

import com.dalimao.mytaxi.common.lbs.LocationInfo;

/**
 * Created by liuguangli on 17/5/31.
 */

public interface IMainManager {

    /**
     *  获取附近司机
     * @param latitude
     * @param longitude
     */
    void fetchNearDrivers(double latitude, double longitude);


    /**
     * 上报位置
     * @param locationInfo
     */

    void updateLocationToServer(LocationInfo locationInfo);
}

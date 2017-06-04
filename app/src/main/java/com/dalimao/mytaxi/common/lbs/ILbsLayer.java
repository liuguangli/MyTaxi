package com.dalimao.mytaxi.common.lbs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import java.util.List;

/**
 * Created by liuguangli on 17/5/30.
 * 定义地图服务通用抽象接口
 */

public interface ILbsLayer {

    /**
     *  获取地图
     */
     View getMapView();

    /**
     *  设置位置变化监听
     */
    void setLocationChangeListener(CommonLocationChangeListener locationChangeListener);

    /**
     *  设置定位图标
     */
    void setLocationRes(int res);
    /**
     *  添加，更新标记点，包括位置、角度（通过 id 识别）
     */
    void addOrUpdateMarker(LocationInfo locationInfo, Bitmap bitmap);

    /**
     *   获取当前城市
     */
    String getCity();

    /**
     * 联动搜索附近的位置
     */
    void poiSearch(String key, OnSearchedListener listener);

    /**
     * 绘制两点之间行车路径
     * @param start
     * @param end
     * @param color
     * @param listener
     */
    void driverRoute(LocationInfo start,
                     LocationInfo end,
                     int color,
                     OnRouteCompleteListener listener);

    /**
     *  生命周期函数
     */

    void onCreate(Bundle state);
    void onResume();
    void onSaveInstanceState(Bundle outState);
    void onPause();
    void onDestroy();

    void clearAllMarkers();


    /**
     * Created by liuguangli on 17/5/30.
     */
    interface CommonLocationChangeListener {
        void onLocationChanged(LocationInfo locationInfo);
        void onLocation(LocationInfo locationInfo);
    }
    /**
     * POI 搜索结果监听器
     * Created by liuguangli
     */
     interface OnSearchedListener {
        void onSearched(List<LocationInfo> results);

        void onError(int rCode);
    }
    /**
     * 路径规划完成监听
     * Created by liuguangli on 17/3/24.
     */
    interface OnRouteCompleteListener {
        void onComplete(RouteInfo result);
    }

    /**
     *  移动相机到两点之间的视野范围
     */
    void moveCamera(LocationInfo locationInfo1,
                    LocationInfo locationInfo2);

    /**
     *  移动动相机到某个点，
     * @param locationInfo
     * @param scale 缩放系数
     */
   void moveCameraToPoint(LocationInfo locationInfo, int scale);

}

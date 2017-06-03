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
     *  生命周期函数
     */

    void onCreate(Bundle state);
    void onResume();
    void onSaveInstanceState(Bundle outState);
    void onPause();
    void onDestroy();

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
}

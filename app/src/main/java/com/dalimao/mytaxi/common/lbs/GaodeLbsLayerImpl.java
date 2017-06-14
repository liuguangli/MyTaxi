package com.dalimao.mytaxi.common.lbs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.dalimao.mytaxi.common.util.LogUtil;
import com.dalimao.mytaxi.common.util.SensorEventHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.rotation;

/**
 * Created by liuguangli on 17/5/30.
 */

public class GaodeLbsLayerImpl implements ILbsLayer {
    private static final String TAG = "GaodeLbsLayerImpl";
    private static final String KEY_MY_MARKERE = "1000";
    private Context mContext;
    //位置定位对象
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    // 地图视图对象
    private MapView mapView;
    // 地图管理对象
    private AMap aMap;
    // 地图位置变化回调对象
    private LocationSource.OnLocationChangedListener mMapLocationChangeListener;
    private boolean firstLocation = true;
    private SensorEventHelper mSensorHelper;
    private CommonLocationChangeListener mLocationChangeListener;
    private MyLocationStyle myLocationStyle;
    // 管理地图标记集合
    private Map<String, Marker> markerMap = new HashMap<>();

    //    当前城市
    private String mCity;
    // 路径查询对象
    private RouteSearch mRouteSearch;

    public GaodeLbsLayerImpl(Context context) {
        // 创建地图对象
        mapView = new MapView(context);
        // 获取地图管理器
        aMap = mapView.getMap();
        // 创建定位对象
        mlocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);

        // 传感器对象
        mSensorHelper = new SensorEventHelper(context);
        mSensorHelper.registerSensorListener();
        mContext = context;
    }

    @Override
    public View getMapView() {
        return mapView;
    }

    @Override
    public void setLocationChangeListener(CommonLocationChangeListener locationChangeListener) {
        mLocationChangeListener = locationChangeListener;
    }

    @Override
    public void setLocationRes(int res) {
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(res));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
    }

    @Override
    public void addOrUpdateMarker(LocationInfo locationInfo, Bitmap bitmap) {
        if (markerMap == null) {
            markerMap = new HashMap<>();
        }
        Marker storedMarker = markerMap.get(locationInfo.getKey());
        LatLng latLng = new LatLng(locationInfo.getLatitude(),
                locationInfo.getLongitude());
        if (storedMarker != null) {
            // 如果已经存在则更新角度、位置
            storedMarker.setPosition(latLng);
            storedMarker.setRotateAngle(locationInfo.getRotation());
        } else {
            // 如果不存在则创建
            MarkerOptions options = new MarkerOptions();
            BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bitmap);
            options.icon(des);
            options.anchor(0.5f, 0.5f);
            options.position(latLng);
            Marker marker = aMap.addMarker(options);
            marker.setRotateAngle(rotation);
            markerMap.put(locationInfo.getKey(), marker);
            if (KEY_MY_MARKERE.equals(locationInfo.getKey())) {
                // 传感器控制我的位置标记的旋转角度
                mSensorHelper.setCurrentMarker(marker);
            }
        }
    }

    @Override
    public String getCity() {
        return mCity;
    }


    /**
     * 重点内容, 高德地图的 POI 搜索接口
     *
     * @param key
     * @param listener
     */
    @Override
    public void poiSearch(String key, final OnSearchedListener listener) {

        if (!TextUtils.isEmpty(key)) {
            // 1 组装关键字
            InputtipsQuery inputQuery = new InputtipsQuery(key, "");
            Inputtips inputTips = new Inputtips(mContext, inputQuery);
            // 2 开始异步搜索
            inputTips.requestInputtipsAsyn();
            // 3 监听处理搜索结果
            inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                @Override
                public void onGetInputtips(List<Tip> tipList, int rCode) {
                    if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                        // 正确返回解析结果
                        List<LocationInfo> locationInfos = new ArrayList<LocationInfo>();

                        for (int i = 0; i < tipList.size(); i++) {
                            Tip tip = tipList.get(i);
                            if (tip.getPoint()!= null) {
                                LocationInfo locationInfo =
                                        new LocationInfo(tip.getPoint().getLatitude(),
                                                tip.getPoint().getLongitude());
                                locationInfo.setName(tip.getName());
                                locationInfos.add(locationInfo);
                            }
                        }
                        listener.onSearched(locationInfos);
                    } else {
                        listener.onError(rCode);
                    }
                }
            });

        }
    }

    /**
     * t 重点内容：两点之间行车路径
     *
     * @param start
     * @param end
     * @param color
     * @param listener
     */
    @Override
    public void driverRoute(LocationInfo start,
                            LocationInfo end,
                            final int color,
                            final OnRouteCompleteListener listener) {

        // 1 组装起点和终点信息
        LatLonPoint startLatLng =
                new LatLonPoint(start.getLatitude(), start.getLongitude());
        LatLonPoint endLatLng =
                new LatLonPoint(end.getLatitude(), end.getLongitude());
        final RouteSearch.FromAndTo fromAndTo =
                new RouteSearch.FromAndTo(startLatLng, endLatLng);
        // 2 创建路径查询参数，
        // 第一个参数表示路径规划的起点和终点，
        // 第二个参数表示驾车模式，
        // 第三个参数表示途经点，
        // 第四个参数表示避让区域，
        // 第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query =
                new RouteSearch.DriveRouteQuery(fromAndTo,
                        RouteSearch.DrivingDefault,
                        null,
                        null,
                        "");
        //  3 创建搜索对象，异步路径规划驾车模式查询
        if (mRouteSearch == null) {
            mRouteSearch = new RouteSearch(mContext);
        }
        // 4 执行搜索
        mRouteSearch.calculateDriveRouteAsyn(query);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                // 1 获取第一条路径
                DrivePath drivePath = driveRouteResult.getPaths()
                        .get(0);
                /**
                 * 2 获取这条路径上所有的点，使用 Polyline 绘制路径
                 */
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(color);
                // 起点
                LatLonPoint startPoint = driveRouteResult.getStartPos();
                // 路径中间步骤
                List<DriveStep> drivePaths = drivePath.getSteps();
                // 路径终点
                LatLonPoint endPoint = driveRouteResult.getTargetPos();
                // 添加起点
                polylineOptions.add(new LatLng(startPoint.getLatitude(),
                        startPoint.getLongitude()));

                /**
                 * 添加中间节点
                 */
                for (DriveStep step : drivePaths) {
                    List<LatLonPoint> latlonPoints = step.getPolyline();
                    for (LatLonPoint latlonpoint : latlonPoints) {
                        LatLng latLng =
                                new LatLng(latlonpoint.getLatitude(), latlonpoint.getLongitude());
                        polylineOptions.add(latLng);

                    }
                }
                // 添加终点
                polylineOptions.add(new LatLng(endPoint.getLatitude(), endPoint.getLongitude()));
                // 执行绘制
                aMap.addPolyline(polylineOptions);
                /**
                 * 3 回调业务
                 */
                if (listener != null) {

                    RouteInfo info = new RouteInfo();
                    info.setTaxiCost(driveRouteResult.getTaxiCost());
                    info.setDuration(10 + new Long(drivePath.getDuration() / 1000 * 60).intValue());
                    info.setDistance(0.5f + drivePath.getDistance() / 1000);
                    listener.onComplete(info);
                }

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
    }


    @Override
    public void onCreate(Bundle savedState) {
        mapView.onCreate(savedState);
        setUpMap();
    }


    private void setUpMap() {

        if (myLocationStyle != null) {
            aMap.setMyLocationStyle(myLocationStyle);
        }

        // 设置地图激活（加载监听）
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mMapLocationChangeListener = onLocationChangedListener;
                LogUtil.d(TAG, "activate");
            }

            @Override
            public void deactivate() {

                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
                mlocationClient = null;
            }
        });
        // 设置默认定位按钮是否显示，这里先不想业务使用方开放
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false，这里先不想业务使用方开放
        aMap.setMyLocationEnabled(true);


    }

    public void setUpLocation() {

        //设置监听器

        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                // 定位变化位置
                if (mMapLocationChangeListener != null) {
                    //  当前城市
                    mCity = aMapLocation.getCity();
                    // 地图已经激活，通知蓝点实时更新
                    mMapLocationChangeListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                    LogUtil.d(TAG, "onLocationChanged");
                    LocationInfo locationInfo = new LocationInfo(aMapLocation.getLatitude(),
                            aMapLocation.getLongitude());
                    locationInfo.setName(aMapLocation.getPoiName());
                    locationInfo.setKey(KEY_MY_MARKERE);
                    if (firstLocation) {
                        firstLocation = false;
                        moveCameraToPoint(locationInfo, 17);
                        if (mLocationChangeListener != null) {

                            mLocationChangeListener.onLocation(locationInfo);
                        }


                    }
                    if (mLocationChangeListener != null) {

                        mLocationChangeListener.onLocationChanged(locationInfo);
                    }
                }
            }
        });
        mlocationClient.startLocation();
    }

    /**
     * 缩放相机
     * @param locationInfo
     * @param scale
     */
    @Override
    public void moveCameraToPoint(LocationInfo locationInfo, int scale) {
        LatLng latLng = new LatLng(locationInfo.getLatitude(),
                locationInfo.getLongitude());
        CameraUpdate up = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, scale, 30, 30));
        aMap.moveCamera(up);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        setUpLocation();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        mlocationClient.stopLocation();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        mlocationClient.onDestroy();
    }

    @Override
    public void clearAllMarkers() {
        aMap.clear();
        markerMap.clear();
    }

    @Override
    public void moveCamera(LocationInfo locationInfo1, LocationInfo locationInfo2) {
        try {
            LatLng latLng =
                    new LatLng(locationInfo1.getLatitude(),
                            locationInfo1.getLongitude());
            LatLng latLng1 =
                    new LatLng(locationInfo2.getLatitude(),
                            locationInfo2.getLongitude());
            LatLngBounds.Builder b = LatLngBounds.builder();
            b.include(latLng);
            b.include(latLng1);
            LatLngBounds latLngBounds = b.build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));
        } catch (Exception e) {
            LogUtil.e(TAG, "moveCamera: " + e.getMessage());
        }
    }
}

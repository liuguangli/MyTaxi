package com.dalimao.mytaxi.main.model;

import com.dalimao.mytaxi.common.databus.RxBus;
import com.dalimao.mytaxi.common.http.IHttpClient;
import com.dalimao.mytaxi.common.http.IRequest;
import com.dalimao.mytaxi.common.http.IResponse;
import com.dalimao.mytaxi.common.http.api.API;
import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;
import com.dalimao.mytaxi.common.http.impl.BaseRequest;
import com.dalimao.mytaxi.common.lbs.LocationInfo;
import com.dalimao.mytaxi.common.util.LogUtil;
import com.dalimao.mytaxi.main.model.response.NearDriversResponse;
import com.google.gson.Gson;

import rx.functions.Func1;

/**
 * Created by liuguangli on 17/5/31.
 */

public class MainMangerImpl implements IMainManager{
    private static final String TAG = "MainMangerImpl";
    IHttpClient mHttpClient;

    public MainMangerImpl(IHttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    @Override
    public void fetchNearDrivers(final double latitude, final double longitude) {

        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                IRequest request = new BaseRequest(API.Config.getDomain()
                        + API.GET_NEAR_DRIVERS);
                request.setBody("latitude", new Double(latitude).toString() );
                request.setBody("longitude", new Double(longitude).toString() );
                IResponse response = mHttpClient.get(request, false);
                if (response.getCode() == BaseBizResponse.STATE_OK) {

                    try {
                        NearDriversResponse nearDriversResponse =
                                new Gson().fromJson(response.getData(),
                                        NearDriversResponse.class);

                        return nearDriversResponse;
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }
        });
    }

    @Override
    public void updateLocationToServer(final LocationInfo locationInfo) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                IRequest request = new BaseRequest(API.Config.getDomain()
                        + API.UPLOAD_LOCATION);
                request.setBody("latitude",
                        new Double(locationInfo.getLatitude()).toString() );
                request.setBody("longitude",
                        new Double(locationInfo.getLongitude()).toString() );
                request.setBody("key",locationInfo.getKey());
                request.setBody("rotation",
                        new Float(locationInfo.getRotation()).toString() );
                IResponse response = mHttpClient.post(request, false);
                if (response.getCode() == BaseBizResponse.STATE_OK) {
                    LogUtil.d(TAG, "位置上报成功");
                } else {
                    LogUtil.d(TAG, "位置上报失败");
                }
                return null;
            }
        });
    }

}

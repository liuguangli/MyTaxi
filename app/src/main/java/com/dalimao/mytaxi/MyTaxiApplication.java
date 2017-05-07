package com.dalimao.mytaxi;

import android.app.Application;

/**
 * Created by liuguangli on 17/5/6.
 */

public class MyTaxiApplication extends Application {
    private static MyTaxiApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance  = this;
    }
    public static MyTaxiApplication getInstance() {
        return  instance;
    }
}

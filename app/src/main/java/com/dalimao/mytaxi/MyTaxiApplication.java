package com.dalimao.mytaxi;

import android.app.Application;

/**
 * Created by jinny on 2018/2/17.
 */

public class MyTaxiApplication extends Application {

    private static MyTaxiApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyTaxiApplication getInstance() {
        return instance;
    }
}

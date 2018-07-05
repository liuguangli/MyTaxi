package yx.taxi;

import android.app.Application;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class TaxiApplication extends Application {
    private static TaxiApplication application;
    @Override
    public void onCreate() {
        super.onCreate( );
        application = this;
    }

    public static TaxiApplication geTaxiApplication(){
        return application;
    }
}

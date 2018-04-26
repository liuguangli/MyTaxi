package yx.taxi;

import android.app.Application;
import android.content.Context;

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

    public static Context geTaxiApplication(){
        return application.getApplicationContext();
    }
}

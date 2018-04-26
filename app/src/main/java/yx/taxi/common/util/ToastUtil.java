package yx.taxi.common.util;

import android.widget.Toast;

import yx.taxi.TaxiApplication;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class ToastUtil {
    public static void show(String msg){
        Toast.makeText(TaxiApplication.geTaxiApplication(), msg, Toast.LENGTH_SHORT).show( );
    }
}

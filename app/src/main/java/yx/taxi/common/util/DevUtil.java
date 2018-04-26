package yx.taxi.common.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

import yx.taxi.TaxiApplication;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class DevUtil {
    public static String UUID() {
        TelephonyManager tm = (TelephonyManager) TaxiApplication.geTaxiApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId + System.currentTimeMillis();
    }
    public static void closeInputMethod(Activity context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

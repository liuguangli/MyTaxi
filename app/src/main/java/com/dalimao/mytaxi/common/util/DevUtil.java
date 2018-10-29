package com.dalimao.mytaxi.common.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

/**
 *
 * 设备相关的工具类
 *
 * Created by jinny on 2018/2/17.
 */

public class DevUtil {
    /**
     * 获取 UID
     * @param context
     * @return
     */
    public static String UUID(Context context) {
        TelephonyManager tm = (TelephonyManager)context
                .getSystemService(Context.TELEPHONY_SERVICE);
        /**拿到一个设备的id*/
        String deviceId = tm.getDeviceId();
        /**这个设备的id有可能会有重复，所以为了降低重复率，在设备id的基础上，再去加上一个时间。*/
        return deviceId + System.currentTimeMillis();
    }

    public static void closeInputMethod(Activity context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

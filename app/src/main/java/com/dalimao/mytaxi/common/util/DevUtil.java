package com.dalimao.mytaxi.common.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 设备相关的工具类
 * Created by liuguangli on 17/3/13.
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
        String deviceId = tm.getDeviceId();
        return deviceId + System.currentTimeMillis();
    }
    public static void closeInputMethod(Activity context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

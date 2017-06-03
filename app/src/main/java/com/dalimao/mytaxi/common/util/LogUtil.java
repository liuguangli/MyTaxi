package com.dalimao.mytaxi.common.util;

import android.util.Log;

/**
 * Created by liuguangli on 17/2/26.
 */

public class LogUtil {

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}

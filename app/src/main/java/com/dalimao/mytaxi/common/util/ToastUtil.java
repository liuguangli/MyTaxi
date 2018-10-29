package com.dalimao.mytaxi.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jinny on 2018/2/16.
 */

public class ToastUtil {

    public static void show(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}

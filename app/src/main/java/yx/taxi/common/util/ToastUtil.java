package yx.taxi.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class ToastUtil {
    public static void show(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show( );
    }
}

package yx.taxi.common.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by yangxiong on 2018/4/26/026.
 */

public class SharedPreferencesDao {
    private static final String TAG = "SharedPreferencesDao";
    public static final String FILE_ACCOUNT = "FILE_ACCOUNT";
    public static final java.lang.String KEY_ACCOUNT = "KEY_ACCOUNT";
    private SharedPreferences sharedPreferences;
    /**
     *  初始化
     */
    public SharedPreferencesDao(Context context ,String fileName) {
        sharedPreferences =context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
    /**
     * 保存 k-v
     */
    public void save(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }
    /**
     *  读取 k-v
     */
    public String get(String key) {

        return sharedPreferences.getString(key, null);
    }

    /**
     * 保存对象
     */
    public void save(String key, Object object) {
        String value = new Gson().toJson(object);
        save(key, value);
    }

    /**
     *  读取对象
     */

    public Object get(String key, Class cls) {

        String value = get(key);
        try {
            if (value != null) {
                Object o = new Gson().fromJson(value, cls);
                return o;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }


        return null;
    }
}

package com.dalimao.mytaxi.common.storage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by jinny on 2018/2/17.
 *
 * SharedPreference数据访问对象
 */

public class SharedPreferencesDao {

    private static final String TAG = "SharedPreferencesDao";
    public static final String FILE_ACCOUNT = "FILE_ACCOUNT";
    public static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private SharedPreferences mSharedPreferences;

    /**
     * 初始化
     */
    public SharedPreferencesDao(Application application, String fileName) {
        mSharedPreferences = application.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存 k-v
     * @param key
     * @param value
     */
    public void save(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 读取 k-v
     * @param key
     * @return
     */
    public String get(String key) {
        return mSharedPreferences.getString(key, null);
    }

    /**
     * 保存对象
     * @param key
     * @param object
     */
    public void save(String key, Object object) {
        String value = new Gson().toJson(object);
        save(key, value);
    }

    /**
     * 读取对象
     * @param key
     * @param cls
     * @return
     */
    public Object get(String key, Class cls) {
        String value = get(key);
        try{
            if (value != null) {
                Object o = new Gson().fromJson(value, cls);
                return o;
            }
        }catch(Exception e){
            Log.i(TAG, e.getMessage());
        }
        return null;
    }
}

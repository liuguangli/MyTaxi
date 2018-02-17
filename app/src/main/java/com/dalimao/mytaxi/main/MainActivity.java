package com.dalimao.mytaxi.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dalimao.mytaxi.MyTaxiApplication;
import com.dalimao.mytaxi.R;
import com.dalimao.mytaxi.account.PhoneInputDialog;
import com.dalimao.mytaxi.account.response.Account;
import com.dalimao.mytaxi.account.response.LoginResponse;
import com.dalimao.mytaxi.common.http.IHttpClient;
import com.dalimao.mytaxi.common.http.IRequest;
import com.dalimao.mytaxi.common.http.IResponse;
import com.dalimao.mytaxi.common.http.api.API;
import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;
import com.dalimao.mytaxi.common.http.impl.BaseRequest;
import com.dalimao.mytaxi.common.http.impl.BaseResponse;
import com.dalimao.mytaxi.common.http.impl.OkHttpClientImpl;
import com.dalimao.mytaxi.common.storage.SharedPreferencesDao;
import com.dalimao.mytaxi.common.util.ToastUtil;
import com.google.gson.Gson;

/**
 * Created by jinny on 2018/2/13.
 *
 * 1、检查本地记录（登录状态检查）
 * 2、若用户没登录则登录
 * 3、登录之前先校验手机号码
 * 4、token 有效使用 token 自动登录
 * TODO：地图初始化
 */

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private IHttpClient mHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHttpClient = new OkHttpClientImpl();

        /**检查用户是否登录*/
        checkLoginState();
    }

    /**
     * 检查用户是否登录
     */
    private void checkLoginState() {

        /**获取本地登录信息  使用SharedPreference*/
        SharedPreferencesDao dao = new SharedPreferencesDao(MyTaxiApplication.getInstance(), SharedPreferencesDao.FILE_ACCOUNT);
        final Account account = (Account) dao.get(SharedPreferencesDao.KEY_ACCOUNT, Account.class);

        /**登录是否过期*/
        boolean tokenValid = false;

        /**检查token是否过期*/
        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                //token是有效的
                tokenValid = true;
            }
        }

        if (!tokenValid) {
            /**用户输入手机号码的弹框*/
            showPhoneInputDialog();
        } else {
            /**请求网络，完成自动登录*/
            new Thread(){
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
                    IRequest request = new BaseRequest(url);
                    request.setBody("token", account.getToken());

                    IResponse response = mHttpClient.post(request, false);
                    Log.i(TAG, response.getData());
                    if (response.getCode() == BaseResponse.STATE_OK) {
                        LoginResponse bizRes = new Gson().fromJson(response.getData(), LoginResponse.class);
                        if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                            //保存登录信息
                            Account account = bizRes.getData();
                            //TODO 加密存储
                            SharedPreferencesDao dao = new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                                    SharedPreferencesDao.FILE_ACCOUNT);
                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                            //通知UI
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(MainActivity.this, getString(R.string.login_suc));
                                }
                            });

                        }
                        if (bizRes.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPhoneInputDialog();
                                }
                            });
                        } else {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(MainActivity.this, getString(R.string.error_server));
                                }
                            });
                        }
                    }
                }
            }.start();
        }
    }

    /**
     * 显示手机输入框
     */
    private void showPhoneInputDialog() {
        PhoneInputDialog dialog = new PhoneInputDialog(this);
        dialog.show();
    }
}

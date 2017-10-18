package com.dalimao.mytaxi.account.model;

import android.os.Handler;
import android.util.Log;

import com.dalimao.mytaxi.MyTaxiApplication;
import com.dalimao.mytaxi.account.model.response.Account;
import com.dalimao.mytaxi.account.model.response.LoginResponse;
import com.dalimao.mytaxi.common.http.IHttpClient;
import com.dalimao.mytaxi.common.http.IRequest;
import com.dalimao.mytaxi.common.http.IResponse;
import com.dalimao.mytaxi.common.http.api.API;
import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;
import com.dalimao.mytaxi.common.http.impl.BaseRequest;
import com.dalimao.mytaxi.common.http.impl.BaseResponse;
import com.dalimao.mytaxi.common.storage.SharedPreferencesDao;
import com.dalimao.mytaxi.common.util.DevUtil;
import com.google.gson.Gson;

/**
 * Created by liuguangli on 17/5/13.
 */

public class AccountManagerImpl implements IAccountManager {
    private static final String TAG = "AccountManagerImpl";



    // 网络请求库
    private IHttpClient httpClient;
    // 数据存储
    private SharedPreferencesDao sharedPreferencesDao;
    // 发送消息 handler
    private Handler handler;

    public AccountManagerImpl(IHttpClient httpClient,
                              SharedPreferencesDao sharedPreferencesDao) {
        this.httpClient = httpClient;
        this.sharedPreferencesDao = sharedPreferencesDao;
    }


    @Override
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * 获取验证码
     * @param phone
     */
    @Override
    public void fetchSMSCode(final String phone) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.GET_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                IResponse response = httpClient.get(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(),
                                    BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        handler.sendEmptyMessage(SMS_SEND_SUC);
                    } else  {
                        handler.sendEmptyMessage(SMS_SEND_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SMS_SEND_FAIL);
                }

            }
        }.start();
    }

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void checkSmsCode(final String phone, final String smsCode) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("code", smsCode);
                IResponse response = httpClient.get(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        handler.sendEmptyMessage(SMS_CHECK_SUC);
                    } else  {
                        handler.sendEmptyMessage(SMS_CHECK_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SMS_CHECK_FAIL);
                }

            }
        }.start();
    }

    /**
     * 检查用户是否存在
     * @param phone
     */
    @Override
    public void checkUserExist(final String phone) {

        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_USER_EXIST;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                IResponse response = httpClient.get(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(),
                                    BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_USER_EXIST) {
                        handler.sendEmptyMessage(USER_EXIST);
                    } else if (bizRes.getCode() ==
                            BaseBizResponse.STATE_USER_NOT_EXIST)  {
                        handler.sendEmptyMessage(USER_NOT_EXIST);
                    }
                } else {
                    handler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    /**
     * 注册
     * @param phone
     * @param password
     */
    @Override
    public void register(final String phone, final String password) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.REGISTER;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("password", password);
                request.setBody("uid", DevUtil.UUID(MyTaxiApplication.getInstance()));

                IResponse response = httpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(),
                                    BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        handler.sendEmptyMessage(REGISTER_SUC);
                    } else {
                        handler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    /**
     * 登录
     * @param phone
     * @param password
     */
    @Override
    public void login(final String phone, final String password) {

        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.LOGIN;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("password", password);


                IResponse response = httpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    LoginResponse bizRes =
                            new Gson().fromJson(response.getData(), LoginResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        // 保存登录信息
                        Account account = bizRes.getData();
                        sharedPreferencesDao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                        // 通知 UI
                        handler.sendEmptyMessage(LOGIN_SUC);
                    } else if (bizRes.getCode() == BaseBizResponse.STATE_PW_ERR){
                        handler.sendEmptyMessage(PW_ERROR);
                    } else {
                        handler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    /**
     * token 自动登录
     */
    @Override
    public void loginByToken() {
        // 获取本地登录信息
        final Account account =
                (Account) sharedPreferencesDao.get(SharedPreferencesDao.KEY_ACCOUNT,
                        Account.class);


        // 登录是否过期
        boolean tokenValid = false;

        // 检查token是否过期

        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                // token 有效
                tokenValid = true;
            }
        }
        if (!tokenValid) {
            handler.sendEmptyMessage(TOKEN_INVALID);
        } else {
            // 请求网络，完成自动登录
            new Thread() {
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
                    IRequest request = new BaseRequest(url);
                    request.setBody("token", account.getToken());
                    IResponse response = httpClient.post(request, false);
                    Log.d(TAG, response.getData());
                    if (response.getCode() == BaseResponse.STATE_OK) {
                        LoginResponse bizRes =
                                new Gson().fromJson(response.getData(), LoginResponse.class);
                        if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                            // 保存登录信息
                            Account account = bizRes.getData();
                            // todo: 加密存储
                            sharedPreferencesDao.save(SharedPreferencesDao.KEY_ACCOUNT, account);
                            handler.sendEmptyMessage(LOGIN_SUC);
                        } if(bizRes.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
                            handler.sendEmptyMessage(TOKEN_INVALID);
                        }
                    } else {
                        handler.sendEmptyMessage(SERVER_FAIL);
                    }

                }
            }.start();
        }
    }
}

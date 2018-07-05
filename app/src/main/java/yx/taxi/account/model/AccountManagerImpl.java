package yx.taxi.account.model;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import yx.taxi.TaxiApplication;
import yx.taxi.account.response.Account;
import yx.taxi.account.response.LoginResponse;
import yx.taxi.common.api.API;
import yx.taxi.common.http.IHttpClient;
import yx.taxi.common.http.IRequest;
import yx.taxi.common.http.IResponse;
import yx.taxi.common.http.biz.BaseBizResponse;
import yx.taxi.common.http.impl.OkHttpClientImpl;
import yx.taxi.common.http.impl.RequestImpl;
import yx.taxi.common.http.impl.ResponseImpl;
import yx.taxi.common.storage.SharedPreferencesDao;
import yx.taxi.common.util.DevUtil;

/**
 * Created by yangxiong on 2018/5/3/003.
 */

public class AccountManagerImpl implements IAccountManager {
    private static final String TAG ="AccountManagerImpl" ;
    private Handler mHandler;
    private IHttpClient mHttpClient;

    public AccountManagerImpl(IHttpClient mIHttpClient) {

        this.mHttpClient = mIHttpClient;
    }

    public void setmHandler(Handler mHandler) {

        this.mHandler = mHandler;
    }

    @Override
    public void fetchSMSCode(final String phone) {
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                String url = API.Config.getDomain( ) + API.GET_SMS_CODE;
                IHttpClient client = new OkHttpClientImpl( );
                IRequest request = new RequestImpl(url);
                request.setBody("phone", phone);
                IResponse response = client.get(request, false);
                int code = response.getCode( );
                if (code == ResponseImpl.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson( ).fromJson(response.getData( ), BaseBizResponse.class);
                    if (bizRes.getCode( ) == BaseBizResponse.STATE_OK) {
                        mHandler.sendEmptyMessage(SMS_SEND_SUC);
                    } else {
                        mHandler.sendEmptyMessage(SMS_SEND_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SMS_SEND_FAIL);
                }
            }
        }).start( );
    }

    @Override
    public void checkSmsCode(final String phone, final String smsCode) {
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                String url = API.Config.getDomain( ) + API.CHECK_SMS_CODE;
                IHttpClient client = new OkHttpClientImpl( );
                IRequest request = new RequestImpl(url);
                request.setBody("phone", phone);
                request.setBody("code", smsCode);
                IResponse response = client.get(request, false);
                int code = response.getCode( );
                if (response.getCode( ) == ResponseImpl.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson( ).fromJson(response.getData( ), BaseBizResponse.class);
                    if (bizRes.getCode( ) == BaseBizResponse.STATE_OK) {
                        mHandler.sendEmptyMessage(SMS_CHECK_SUC);
                    } else {
                        mHandler.sendEmptyMessage(SMS_CHECK_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SMS_CHECK_FAIL);
                }

            }
        }).start( );
    }

    @Override
    public void checkUserExist(final String phone) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_USER_EXIST;
                IRequest request = new RequestImpl(url);
                request.setBody("phone", phone);
                IResponse response = mHttpClient.get(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == ResponseImpl.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_USER_EXIST) {
                        mHandler.sendEmptyMessage(USER_EXIST);
                    } else if (bizRes.getCode() == BaseBizResponse.STATE_USER_NOT_EXIST)  {
                        mHandler.sendEmptyMessage(USER_NOT_EXIST);
                    }
                } else {
                   mHandler.sendEmptyMessage(SERVER_FAIL );
                }

            }
        }.start();
    }

    @Override
    public void register(final String phonePhone, final String password) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.REGISTER;
                IRequest request = new RequestImpl(url);
                request.setBody("phone", phonePhone);
                request.setBody("password", password);
                request.setBody("uid", DevUtil.UUID(TaxiApplication.geTaxiApplication()));

                IResponse response = mHttpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == ResponseImpl.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        mHandler.sendEmptyMessage(REGISTER_SUC);
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void login(final String mPhoneStr, final String password) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.LOGIN;
                IRequest request = new RequestImpl(url);
                request.setBody("phone", mPhoneStr);
                request.setBody("password", password);


                IResponse response = mHttpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == ResponseImpl.STATE_OK) {
                    LoginResponse bizRes =
                            new Gson().fromJson(response.getData(), LoginResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        // 保存登录信息
                        Account account = bizRes.getData();
                        SharedPreferencesDao dao =
                                new SharedPreferencesDao(TaxiApplication.geTaxiApplication(),SharedPreferencesDao.FILE_ACCOUNT);
                        dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                        // 通知 UI
                        mHandler.sendEmptyMessage(LOGIN_SUC);
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void loginByToken() {

    }
}

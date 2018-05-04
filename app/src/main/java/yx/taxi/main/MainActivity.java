package yx.taxi.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.yangxiong.mytaxi.R;

import yx.taxi.TaxiApplication;
import yx.taxi.account.PhoneInputDialog;
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
import yx.taxi.common.util.ToastUtil;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 12312;
    private static final String TAG = "MainActivity";
    private IHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOkHttpClient = new OkHttpClientImpl( );
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
        checkLoginState();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }
    private void checkLoginState() {
        // 获取本地登录信息

        SharedPreferencesDao dao =
                new SharedPreferencesDao(TaxiApplication.geTaxiApplication(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        final Account account =
                (Account) dao.get(SharedPreferencesDao.KEY_ACCOUNT, Account.class);


        boolean isTokenValid = false;
        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                // token 有效
                isTokenValid = true;
            }
        }
        if (isTokenValid){//token信息有效 请求网络完成自动登录
            // 请求网络，完成自动登录
            new Thread() {
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
                    IRequest request = new RequestImpl(url);
                    request.setBody("token", account.getToken());
                    IResponse response = mOkHttpClient.post(request, false);
                    Log.d(TAG, response.getData());
                    if (response.getCode() == ResponseImpl.STATE_OK) {
                        LoginResponse bizRes =
                                new Gson().fromJson(response.getData(), LoginResponse.class);
                        if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                            // 保存登录信息
                            Account account = bizRes.getData();
                            // todo: 加密存储
                            SharedPreferencesDao dao =
                                    new SharedPreferencesDao(TaxiApplication.geTaxiApplication(),
                                            SharedPreferencesDao.FILE_ACCOUNT);
                            String value = new Gson().toJson(account);
                            Log.d(TAG, "run: "+value);
                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);


                            // 通知 UI
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(MainActivity.this,
                                            getString(R.string.login_suc));
                                }
                            });
                        } if(bizRes.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPhoneInputDlg();
                                }
                            });
                        }
                    } else {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(MainActivity.this,
                                        getString(R.string.error_server));
                            }
                        });
                    }

                }
            }.start();
        }else{//过期 跳转到电话输入界面登陆框或注册框
                showPhoneInputDlg();
        }
    }

    private void showPhoneInputDlg() {
        PhoneInputDialog dlg = new PhoneInputDialog(this);
        dlg.show();
    }


}

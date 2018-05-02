package yx.taxi.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yangxiong.mytaxi.R;

import yx.taxi.account.PhoneInputDialog;
import yx.taxi.common.http.IHttpClient;
import yx.taxi.common.http.impl.OkHttpClientImpl;

public class MainActivity extends AppCompatActivity {

    private IHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOkHttpClient = new OkHttpClientImpl( );
        checkLoginState();
    }

    private void checkLoginState() {


        boolean isTokenValid = false;

        if (isTokenValid){//token信息有效 请求网络完成自动登录

        }else{//过期 跳转到电话输入界面登陆框或注册框
                showPhoneInputDlg();
        }
    }

    private void showPhoneInputDlg() {
        PhoneInputDialog dlg = new PhoneInputDialog(this);
        dlg.show();
    }
}

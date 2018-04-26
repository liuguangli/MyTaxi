package yx.taxi.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yangxiong.mytaxi.R;

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


    }
}

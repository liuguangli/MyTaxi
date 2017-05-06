package com.dalimao.mytaxi.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dalimao.mytaxi.R;
import com.dalimao.mytaxi.account.PhoneInputDialog;


/**
 *  1 检查本地纪录(登录态检查)
 *  2 若用户没登录则登录
 *  3 登录之前先校验手机号码
 *  todo : 地图初始化
 */
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLoginState();
    }

    /**
     *  检查用户是否登录
     */
    private void checkLoginState() {

        //  todo: 获取本地登录信息



        // 登录是否过期
        boolean tokenValid = false;

        // TODO: 17/5/3  检查token是否过期

        if (!tokenValid) {
            showPhoneInputDialog();
        } else {
            // TODO:   请求网络，完成自动登录
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

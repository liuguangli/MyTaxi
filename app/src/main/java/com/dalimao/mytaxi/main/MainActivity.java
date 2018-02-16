package com.dalimao.mytaxi.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dalimao.mytaxi.R;
import com.dalimao.mytaxi.account.PhoneInputDialog;

/**
 * Created by jinny on 2018/2/13.
 *
 * 1、检查本地记录（登录状态检查）
 * 2、若用户没登录则登录
 * 3、登录之前先校验手机号码
 * TODO：地图初始化
 */

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**检查用户是否登录*/
        checkLoginState();
    }

    /**
     * 检查用户是否登录
     */
    private void checkLoginState() {

        //TODO：获取本地登录信息


        //登录是否过期
        boolean tokenValid = false;

        //TODO：检查token是否过期
        if (!tokenValid) {
            /**用户输入手机号码的弹框*/
            showPhoneInputDialog();
        } else {
            //TODO: 请求网络，完成自动登录

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

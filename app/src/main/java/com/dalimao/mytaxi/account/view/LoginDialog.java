package com.dalimao.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dalimao.mytaxi.MyTaxiApplication;
import com.dalimao.mytaxi.R;
import com.dalimao.mytaxi.account.model.AccountManagerImpl;
import com.dalimao.mytaxi.account.model.IAccountManager;
import com.dalimao.mytaxi.account.model.response.Account;
import com.dalimao.mytaxi.account.model.response.LoginResponse;
import com.dalimao.mytaxi.account.presenter.ICreatePasswordDialogPresenter;
import com.dalimao.mytaxi.account.presenter.ILoginDialogPresenter;
import com.dalimao.mytaxi.account.presenter.LoginDialogPresenterImpl;
import com.dalimao.mytaxi.common.databus.RxBus;
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
import com.dalimao.mytaxi.main.view.MainActivity;
import com.google.gson.Gson;

import java.lang.ref.SoftReference;


/**
 * 登录框
 * Created by liuguangli on 17/2/26.
 */

public class LoginDialog extends Dialog implements ILoginView {


    private static final String TAG = "LoginDialog";
    private TextView mPhone;
    private EditText mPw;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private String mPhoneStr;
    private ILoginDialogPresenter mPresenter;
    private MainActivity mainActivity;



    public LoginDialog(MainActivity context, String phone) {
        this(context, R.style.Dialog);
        mPhoneStr = phone;
        IHttpClient httpClient = new OkHttpClientImpl();
        SharedPreferencesDao dao =
                new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        IAccountManager accountManager = new AccountManagerImpl(httpClient, dao);
        mPresenter = new LoginDialogPresenterImpl(this, accountManager);
        mainActivity = context;

    }

    public LoginDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_login_input, null);
        setContentView(root);
        initViews();

        // 注册 Presenter
        RxBus.getInstance().register(mPresenter);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        // 注销 Presenter
        RxBus.getInstance().unRegister(mPresenter);
    }

    private void initViews() {
        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.password);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        mPhone.setText(mPhoneStr);

    }

    /**
     * 提交登录
     */
    private void submit() {

        String password = mPw.getText().toString();
        //  网络请求登录
        mPresenter.requestLogin(mPhoneStr, password);
    }


    /**
     * 显示／隐藏 loading
     * @param show
     */

    private void showOrHideLoading(boolean show) {
        if (show) {
            mLoading.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 处理登录成功 UI
     */
    @Override
    public void showLoginSuc() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        mTips.setText(getContext().getString(R.string.login_suc));
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
       mainActivity.showLoginSuc();
        dismiss();

    }

    @Override
    public void showLoading() {
        showOrHideLoading(true);
    }

    @Override
    public void showError(int code, String msg) {

        switch (code) {
            case IAccountManager.PW_ERROR:
                // 密码错误
                showPasswordError();
                break;
            case IAccountManager.SERVER_FAIL:
                // 服务器错误
                showServerError();
                break;
        }
    }
    /**
     *  显示服服务器出错
     */

    private void showServerError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.error_server));
    }


    /**
     * 密码错误
     */
    private void showPasswordError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.password_error));
    }
}

package com.dalimao.mytaxi.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dalimao.mytaxi.MyTaxiApplication;
import com.dalimao.mytaxi.R;
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

import java.lang.ref.SoftReference;

/**
 * Created by jinny on 2018/2/17.
 *
 * 登录
 */

public class LoginDialog extends Dialog {

    private static final String TAG            = "LoginDialog";
    private static final int    LOGIN_SUCCESS  = 1;
    private static final int    SERVER_FAILURE = 2;
    private static final int    PW_ERR         = 4;
    private TextView    mPhone;
    private EditText    mPw;
    private Button      mBtnConfirm;
    private View        mLoading;
    private TextView    mTips;
    private String      mPhoneStr;
    private IHttpClient mHttpClient;
    private MyHandler   mHandler;

    /**
     * 接收子线程消息的Handler
     */
    private static class MyHandler extends Handler {
        //软引用
        SoftReference<LoginDialog> dialogRef;

        public MyHandler(LoginDialog dialog) {
            dialogRef = new SoftReference<LoginDialog>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginDialog loginDialog = dialogRef.get();
            if (loginDialog == null) {
                return;
            }

            //处理UI
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    loginDialog.showLoginSuccess();
                    break;
                case PW_ERR:
                    loginDialog.showPasswordError();
                    break;
                case SERVER_FAILURE:
                    loginDialog.showServerError();
                    break;
            }
        }
    }

    public LoginDialog(Context context, String phone) {
        this(context, R.style.Dialog);
        this.mPhoneStr = phone;
        mHttpClient = new OkHttpClientImpl();
        mHandler = new MyHandler(this);
    }

    public LoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_login_input, null);
        setContentView(root);
        initViews();
    }

    @Override
    public void dismiss() {
        super.dismiss();
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

        //网络请求
        new Thread(){
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.LOGIN;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", mPhoneStr);
                String password = mPw.getText().toString();
                request.setBody("password", password);

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
                        mHandler.sendEmptyMessage(LOGIN_SUCCESS);

                    }
                    if (bizRes.getCode() == BaseBizResponse.STATE_PW_ERR) {
                        mHandler.sendEmptyMessage(PW_ERR);
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAILURE);
                    }
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAILURE);
                }
            }
        }.start();
    }

    /**
     * 显示和隐藏Loading的方法
     */
    public void showOrHideLoading(boolean show) {
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
    public void showLoginSuccess() {
        mLoading.setVisibility(View.GONE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setText(R.string.login_suc);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
        dismiss();
    }

    /**
     * 显示服务器出错
     */
    public void showServerError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setText(getContext().getString(R.string.error_server));
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
    }

    /**
     * 密码错误
     */
    public void showPasswordError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setText(getContext().getString(R.string.password_error));
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
    }
}

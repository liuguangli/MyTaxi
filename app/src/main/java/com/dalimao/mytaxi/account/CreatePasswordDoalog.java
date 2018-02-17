package com.dalimao.mytaxi.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
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
import com.dalimao.mytaxi.common.util.DevUtil;
import com.dalimao.mytaxi.common.util.ToastUtil;
import com.google.gson.Gson;

import java.lang.ref.SoftReference;

/**
 * Created by jinny on 2018/2/13.
 * 密码创建/修改
 */

public class CreatePasswordDoalog extends Dialog{

    private static final String TAG           = "CreatePasswordDoalog";
    private static final int REGISTER_SUCCESS = 1;
    private static final int SERVER_FAILURE   = 100;
    private static final int LOGIN_SUCCESS    = 2;
    private TextView mTitle;
    private TextView mPhone;
    private EditText mPw;
    private EditText mPw1;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private IHttpClient mHttpClient;
    private String mPhoneStr;
    private MyHandler mHandler;

    /**
     * 接收子线程消息的Handler
     */
    private static class MyHandler extends Handler {

        //软引用
        SoftReference<CreatePasswordDoalog> codeDialogRef;
        public MyHandler(CreatePasswordDoalog codeDialog) {
            codeDialogRef = new SoftReference<CreatePasswordDoalog>(codeDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CreatePasswordDoalog dialog = codeDialogRef.get();
            if (dialog == null) {
                return;
            }

            //处理UI的变化
            switch (msg.what) {
                case REGISTER_SUCCESS:
                    dialog.showRegisterSuccess();
                    break;
                case LOGIN_SUCCESS:
                    dialog.showLoginSuccess();
                    break;
                case SERVER_FAILURE:
                    dialog.showServerError();
                    break;
            }
        }
    }

    private void showLoginSuccess() {
        dismiss();
        ToastUtil.show(getContext(),getContext().getString(R.string.login_suc));
    }

    private void showServerError() {
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.error_server));
    }

    /**
     * 处理注册成功
     */
    private void showRegisterSuccess() {
        mLoading.setVisibility(View.VISIBLE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        mTips.setText(getContext().getString(R.string.register_suc_and_loging));

        /**请求网络，完成自动登录*/
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
                        SharedPreferencesDao dao = new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                                SharedPreferencesDao.FILE_ACCOUNT);
                        dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                        //通知UI
                        mHandler.sendEmptyMessage(LOGIN_SUCCESS);




                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAILURE);
                    }
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAILURE);
                }
            }
        }.start();
    }

    public CreatePasswordDoalog(Context context,String phone) {
        this(context, R.style.Dialog);
        //上一个页面的传来的手机号
        this.mPhoneStr = phone;
        this.mHttpClient = new OkHttpClientImpl();
        mHandler = new MyHandler(this);
    }

    public CreatePasswordDoalog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CreatePasswordDoalog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_create_pw, null);
        setContentView(root);
        initViews();
    }

    private void initViews() {
        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.pw);
        mPw1 = (EditText) findViewById(R.id.pw1);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        mTitle = (TextView) findViewById(R.id.dialog_title);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交注册
                submit();
            }
        });

        mPhone.setText(mPhoneStr);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 提交注册
     */
    private void submit() {
        if (checkPassWord()) {
            final String password = mPw.getText().toString();
            final String phonePhone = mPhoneStr;
            //请求网络，提交注册
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.REGISTER;
                    IRequest request = new BaseRequest(url);
                    request.setBody("phone", phonePhone);
                    request.setBody("password", password);
                    request.setBody("uid", DevUtil.UUID(getContext()));
                    /**使用post方法来提交*/
                    IResponse response = mHttpClient.post(request, false);
                    Log.i(TAG, response.getData());
                    if (response.getCode() == BaseResponse.STATE_OK) {
                        BaseBizResponse bizRes = new Gson().fromJson(response.getData(), BaseBizResponse.class);
                        if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                            mHandler.sendEmptyMessage(REGISTER_SUCCESS);
                        } else {
                            mHandler.sendEmptyMessage(SERVER_FAILURE);
                        }
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAILURE);
                    }
                }
            }).start();
        }
    }

    /**
     * 检查密码输入
     * @return
     */
    private boolean checkPassWord() {
        String password = mPw.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext().getString(R.string.password_is_null));
            mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
            return false;
        }

        if (!password.equals(mPw1.getText().toString())) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext().getString(R.string.password_is_not_equal));
            mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
            return false;
        }
        return true;
    }
}

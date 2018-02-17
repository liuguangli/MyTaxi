package com.dalimao.mytaxi.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dalimao.corelibrary.VerificationCodeInput;
import com.dalimao.mytaxi.R;
import com.dalimao.mytaxi.common.http.IHttpClient;
import com.dalimao.mytaxi.common.http.IRequest;
import com.dalimao.mytaxi.common.http.IResponse;
import com.dalimao.mytaxi.common.http.api.API;
import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;
import com.dalimao.mytaxi.common.http.impl.BaseRequest;
import com.dalimao.mytaxi.common.http.impl.BaseResponse;
import com.dalimao.mytaxi.common.http.impl.OkHttpClientImpl;
import com.dalimao.mytaxi.common.util.ToastUtil;
import com.google.gson.Gson;

import java.lang.ref.SoftReference;

/**
 * Created by jinny on 2018/2/13.
 */

public class SmsCodeDialog extends Dialog{

    private static final String TAG              = "SmsCodeDialog";
    private static final int SMS_SEND_SUCCESS    = 1;
    private static final int SMS_SEND_FAILURE    = -1;
    private static final int SMS_CHECK_SUCCESS   = 2;
    private static final int SMS_CHECK_FAILURE   = -2;
    private static final int USER_EXIST          = 3;
    private static final int USER_NOT_EXIST      = -3;
    private static final int USER_SERVER_FAILURE = 100;

    private String mPhone;
    private Button mResentBtn;
    private VerificationCodeInput mVerificationCodeInput;
    private View mLoading;
    private View mErrorView;
    private TextView mPhoneTv;
    private IHttpClient mHttpClient;
    private MyHandler myHandler;

    /**
     * 验证码倒计时
     */
    private CountDownTimer mCountDownTimer = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mResentBtn.setEnabled(false);
            mResentBtn.setText(String.format(getContext().getString(R.string.after_time_resend), millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            mResentBtn.setEnabled(true);
            mResentBtn.setText("重新发送");
            cancel();
        }
    };

    public void showVerifyState(boolean success) {

        if (!success) {
            //提示验证码错误
            mErrorView.setVisibility(View.VISIBLE);
            mVerificationCodeInput.setEnabled(true);
            mLoading.setVisibility(View.GONE);
        } else {

            mErrorView.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);

            /**检查用户是否存在*/
            new Thread(){
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.CHECK_USER_EXIST;
                    IRequest request = new BaseRequest(url);
                    request.setBody("phone", mPhone);
                    IResponse response = mHttpClient.get(request, false);
                    Log.i(TAG, response.getData());
                    if (response.getCode() == BaseResponse.STATE_OK) {
                        BaseBizResponse bizResponse = new Gson().fromJson(response.getData(), BaseBizResponse.class);

                        if (bizResponse.getCode() == BaseBizResponse.STATE_USER_EXIST) {
                            myHandler.sendEmptyMessage(USER_EXIST);
                        } else if (bizResponse.getCode() == BaseBizResponse.STATE_USER_NOT_EXIST) {
                            myHandler.sendEmptyMessage(USER_NOT_EXIST);
                        }
                    } else {
                        myHandler.sendEmptyMessage(USER_SERVER_FAILURE);
                    }
                }
            }.start();
        }
    }


    public void showUserExist(boolean exist) {

        mLoading.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        dismiss();

        if (!exist) {
            /**用户不存在，进入注册*/
            CreatePasswordDoalog createPasswordDoalog = new CreatePasswordDoalog(getContext(), mPhone);
            createPasswordDoalog.show();
        } else {
            /**用户存在，进入登录*/


        }
    }

    /**
     * 接收子线程消息的Handler
     */
    static class MyHandler extends Handler {
        //软引用
        SoftReference<SmsCodeDialog> mCodeDialogSoftReference;

        public MyHandler(SmsCodeDialog codeDialog) {
            mCodeDialogSoftReference = new SoftReference<SmsCodeDialog>(codeDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            SmsCodeDialog codeDialog = mCodeDialogSoftReference.get();
            if (codeDialog == null) {
                return;
            }

            //处理UI 变化
            switch (msg.what) {
                case SMS_SEND_SUCCESS:
                    //信息发送成功
                    codeDialog.mCountDownTimer.start();
                    break;
                case SMS_SEND_FAILURE:
                    //信息发送失败
                    ToastUtil.show(codeDialog.getContext(), codeDialog.getContext().getString(R.string.sms_send_fail));
                    break;
                case SMS_CHECK_SUCCESS:
                    //验证码校验成功
                    codeDialog.showVerifyState(true);
                    break;
                case SMS_CHECK_FAILURE:
                    //验证码校验失败
                    codeDialog.showVerifyState(false);
                    break;
                case USER_EXIST:
                    //用户存在
                    codeDialog.showUserExist(true);
                    break;
                case USER_NOT_EXIST:
                    //用户不存在
                    codeDialog.showUserExist(false);
                    break;
                case USER_SERVER_FAILURE:
                    //服务器不存在，给个简单的提示
                    ToastUtil.show(codeDialog.getContext(), codeDialog.getContext().getString(R.string.error_server));
                    break;
            }
        }
    }

    public SmsCodeDialog(Context context, String phone) {
        this(context, R.style.Dialog);
        /**上一个界面传来的手机号*/
        this.mPhone = phone;
        mHttpClient = new OkHttpClientImpl();
        myHandler = new MyHandler(this);
    }

    public SmsCodeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public SmsCodeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_smscode_input, null);
        setContentView(root);
        mPhoneTv = (TextView) findViewById(R.id.phone);
        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
        mResentBtn = (Button) findViewById(R.id.btn_resend);
        mVerificationCodeInput = (VerificationCodeInput) findViewById(R.id.verificationCodeInput);
        mLoading = findViewById(R.id.loading);
        mErrorView = findViewById(R.id.error);
        mErrorView.setVisibility(View.GONE);
        initListeners();

        //请求下发验证码
        requestSendSmsCode();
    }

    /**
     * 请求下发验证码
     */
    private void requestSendSmsCode() {

        new Thread(){
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.GET_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", mPhone);
                IResponse response = mHttpClient.get(request, false);

                Log.i(TAG, response.getData());

                /**解析之前，对Http的返回做一个判断*/
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizResponse = new Gson().fromJson(response.getData(), BaseBizResponse.class);

                    if (bizResponse.getCode() == BaseBizResponse.STATE_OK) {
                        myHandler.sendEmptyMessage(SMS_SEND_SUCCESS);
                    } else {
                        myHandler.sendEmptyMessage(SMS_SEND_FAILURE);
                    }
                } else {
                    myHandler.sendEmptyMessage(SMS_SEND_FAILURE);
                }
            }
        }.start();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCountDownTimer.cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initListeners() {
        /**关闭按钮注册监听器*/
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /**重发验证码按钮注册监听器*/
        mResentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });

        /**验证码输入完成监听器*/
        mVerificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String code) {
                commit(code);
            }
        });
    }

    private void commit(final String code) {
        showLoading();

        /**
         * 网络请求校验验证码
         */
        new Thread(){
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", mPhone);
                request.setBody("code", code);

                IResponse response = mHttpClient.get(request, false);

                Log.i(TAG, response.getData());

                /**解析之前，对Http的返回做一个判断*/
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizResponse = new Gson().fromJson(response.getData(), BaseBizResponse.class);

                    if (bizResponse.getCode() == BaseBizResponse.STATE_OK) {
                        myHandler.sendEmptyMessage(SMS_CHECK_SUCCESS);
                    } else {
                        myHandler.sendEmptyMessage(SMS_CHECK_FAILURE);
                    }
                } else {
                    myHandler.sendEmptyMessage(SMS_CHECK_FAILURE);
                }
            }
        }.start();

    }

    private void resend() {
        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
    }

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }
}

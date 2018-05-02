package yx.taxi.account;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.google.gson.Gson;
import com.yangxiong.mytaxi.R;

import java.lang.ref.WeakReference;

import yx.taxi.common.api.API;
import yx.taxi.common.http.IHttpClient;
import yx.taxi.common.http.IRequest;
import yx.taxi.common.http.IResponse;
import yx.taxi.common.http.biz.BaseBizResponse;
import yx.taxi.common.http.impl.OkHttpClientImpl;
import yx.taxi.common.http.impl.RequestImpl;
import yx.taxi.common.http.impl.ResponseImpl;
import yx.taxi.common.util.ToastUtil;

/**
 * Created by yangxiong on 2018/5/2/002.
 */

class SmsCodeDialog extends Dialog {
    private static final String TAG = "SmsCodeDialog";
    private static final int SMS_SEND_SUC = 1;
    private static final int SMS_SEND_FAIL = -1;
    private static final int SMS_CHECK_SUC = 2;
    private static final int SMS_CHECK_FAIL = -2;
    private static final int USER_EXIST = 3;
    private static final int USER_NOT_EXIST = -3;
    private static final int SMS_SERVER_FAIL = 100;
    private String mPhone;
    private Button mResentBtn;
    private VerificationCodeInput mVerificationInput;
    private View mLoading;
    private View mErrorView;
    private TextView mPhoneTv;
    private IHttpClient mHttpClient;
    private MyHandler mHandler;

    public SmsCodeDialog(@NonNull Context context) {
        super(context);
    }

    public SmsCodeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected SmsCodeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public SmsCodeDialog(Context context, String s) {
        super(context);
        this.mPhone = s;
        mHttpClient = new OkHttpClientImpl();
        mHandler = new MyHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext( )
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_smscode_input, null);
        setContentView(root);
        mPhoneTv = (TextView) findViewById(R.id.phone);
        String template = getContext( ).getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
        mResentBtn = (Button) findViewById(R.id.btn_resend);
        mVerificationInput = (VerificationCodeInput) findViewById(R.id.verificationCodeInput);
        mLoading = findViewById(R.id.loading);
        mErrorView = findViewById(R.id.error);
        mErrorView.setVisibility(View.GONE);

        initListener( );
        requestSendSmsCode( );
    }

    private void requestSendSmsCode() {

        new Thread(new Runnable( ) {
            @Override
            public void run() {
                String url = API.Config.getDomain( ) + API.GET_SMS_CODE;
                IHttpClient client = new OkHttpClientImpl( );
                IRequest request = new RequestImpl(url);
                request.setBody("phone", mPhone);
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

    private void initListener() {
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View v) {
                dismiss( );
            }
        });
        // 重发验证码按钮注册监听器
        mResentBtn.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                resend( );
            }
        });

        // 验证码输入完成监听器
        mVerificationInput.setOnCompleteListener(new VerificationCodeInput.Listener( ) {
            @Override
            public void onComplete(String code) {

                commit(code);
            }
        });

    }

    private void commit(final String code) {
        showLoading( );
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                String url = API.Config.getDomain( ) + API.CHECK_SMS_CODE;
                IHttpClient client = new OkHttpClientImpl( );
                IRequest request = new RequestImpl(url);
                request.setBody("phone", mPhone);
                request.setBody("code", code);
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

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    private void resend() {
        mCountDownTimer.start();
        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
        mResentBtn.setTextColor(Color.GRAY);
    }
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCountDownTimer.cancel();

    }
    private static class MyHandler extends Handler {
        WeakReference<SmsCodeDialog> reference;

        public MyHandler(SmsCodeDialog smsCodeDialog) {
            reference = new WeakReference<SmsCodeDialog>(smsCodeDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            SmsCodeDialog dialog = reference.get( );
            if (dialog != null) {
                switch (msg.what) {
                    case SmsCodeDialog.SMS_SEND_SUC:

                        dialog.showSendState(true);
                        break;
                    case SmsCodeDialog.SMS_SEND_FAIL:
                        dialog.showSendState(false);

                        break;
                    case SmsCodeDialog.SMS_CHECK_SUC:
                        //验证码校验成功
                        dialog.showVerifyState(true);
                        break;
                    case SmsCodeDialog.SMS_CHECK_FAIL:
                        //验证码校验失败
                        dialog.showVerifyState(false);

                        break;
                    case SmsCodeDialog.USER_EXIST:
                        // 用户存在
                        dialog.showUserExist(true);
                        break;
                    case SmsCodeDialog.USER_NOT_EXIST:
                        // 用户不存在
                        dialog.showUserExist(false);
                        break;
                    case SmsCodeDialog.SMS_SERVER_FAIL:
                        // 服务器错误
                        ToastUtil.show(dialog.getContext().getString(R.string.error_server));
                        break;
                }
            } else {
                return;
            }
        }
    }

    private void showSendState(boolean b) {
        if (b) {
            mPhoneTv.setText(String.format(getContext().getString(R.string.sms_code_send_phone), mPhone));
            mCountDownTimer.start();
        } else {
            ToastUtil.show(getContext().getString(R.string.sms_send_fail));
            mResentBtn.setEnabled(true);
            mResentBtn.setText(getContext().getString(R.string.resend));
            cancel();
        }
    }

    private void showVerifyState(boolean b) {
        if (!b) {

            //提示验证码错误
            mErrorView.setVisibility(View.VISIBLE);
            mVerificationInput.setEnabled(true);
            mLoading.setVisibility(View.GONE);
        } else {

            mErrorView.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            // 检查用户是否存在
            new Thread() {
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.CHECK_USER_EXIST;
                    IRequest request = new RequestImpl(url);
                    request.setBody("phone", mPhone);
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
                        mHandler.sendEmptyMessage(SMS_SERVER_FAIL);
                    }

                }
            }.start();

        }
    }

    private void showUserExist(boolean b) {
        mLoading.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        dismiss();
        if (!b) {
            // 用户不存在,进入注册
            CreatePasswordDialog dialog =
                    new CreatePasswordDialog(getContext(), mPhone);
            dialog.show();

        } else {
            // 用户存在 ，进入登录
            LoginDialog dialog = new LoginDialog(getContext(), mPhone);
            dialog.show();

        }
    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(10000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mResentBtn.setEnabled(false);
            mResentBtn.setText(String.format(getContext()
                    .getString(R.string.after_time_resend,
                            millisUntilFinished/1000)));
        }

        @Override
        public void onFinish() {
            mResentBtn.setTextColor(Color.BLACK);
            mResentBtn.setEnabled(true);
            mResentBtn.setText(getContext().getString(R.string.resend));
            cancel();
        }
    };
}

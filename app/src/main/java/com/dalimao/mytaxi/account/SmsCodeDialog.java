package com.dalimao.mytaxi.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.dalimao.mytaxi.common.http.impl.BaseRequest;
import com.dalimao.mytaxi.common.http.impl.OkHttpClientImpl;

/**
 * Created by jinny on 2018/2/13.
 */

public class SmsCodeDialog extends Dialog{

    private static final String TAG = "SmsCodeDialog";
    private String mPhone;
    private Button mResentBtn;
    private VerificationCodeInput mVerificationCodeInput;
    private View mLoading;
    private View mErrorView;
    private TextView mPhoneTv;
    private IHttpClient mHttpClient;

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

    public SmsCodeDialog(Context context, String phone) {
        this(context, R.style.Dialog);
        /**上一个界面传来的手机号*/
        this.mPhone = phone;
        mHttpClient = new OkHttpClientImpl();
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

    private void commit(String code) {
        showLoading();

        //TODO： 网络请求校验验证码
    }

    private void resend() {
        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
    }

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }
}

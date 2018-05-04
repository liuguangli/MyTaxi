package yx.taxi.account;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.yangxiong.mytaxi.R;

import yx.taxi.account.model.AccountManagerImpl;
import yx.taxi.account.model.IAccountManager;
import yx.taxi.account.presenter.ISmsCodeDialogPresenter;
import yx.taxi.account.presenter.SmsCodeDialogPresenterImpl;
import yx.taxi.account.view.ISmsCodeDialogView;
import yx.taxi.common.http.IHttpClient;
import yx.taxi.common.http.impl.OkHttpClientImpl;
import yx.taxi.common.util.ToastUtil;

/**
 * Created by yangxiong on 2018/5/2/002.
 */

class SmsCodeDialog extends Dialog implements ISmsCodeDialogView {
    private static final String TAG = "SmsCodeDialog";
    private Context context;
    private String mPhone;
    private Button mResentBtn;
    private VerificationCodeInput mVerificationInput;
    private View mLoading;
    private View mErrorView;
    private TextView mPhoneTv;
    private IHttpClient mHttpClient;
    private ISmsCodeDialogPresenter mPresenter;

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
        this.context = context;
        this.mPhone = s;
        mHttpClient = new OkHttpClientImpl();
        mPresenter = new SmsCodeDialogPresenterImpl(this,new AccountManagerImpl(mHttpClient));
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
        mPresenter.requestSendSmsCode(mPhone);
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
        mPresenter.requestCheckSmsCode(mPhone,code);
    }

    @Override
    public void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(int Code, String msg) {
        mLoading.setVisibility(View.GONE);
        switch (Code) {
            case IAccountManager.SMS_SEND_FAIL:
                ToastUtil.show(getContext(),
                        getContext().getString(R.string.sms_send_fail));
                break;
            case IAccountManager.SMS_CHECK_FAIL:
                // 提示验证码错误
                mErrorView.setVisibility(View.VISIBLE);
                mVerificationInput.setEnabled(true);
                break;
            case IAccountManager.SERVER_FAIL:
                ToastUtil.show(getContext(),
                        getContext().getString(R.string.error_server));
                break;
        }
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

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void showUserExist(boolean b) {
        mLoading.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        Log.d(TAG, "showUserExist: "+b);

        if (!b) {
            // 用户不存在,进入注册
            CreatePasswordDialog dialog =
                    new CreatePasswordDialog(context, mPhone);
            dialog.show();

        } else {
            // 用户存在 ，进入登录
            LoginDialog dialog = new LoginDialog(context, mPhone);
            dialog.show();

        }
        dismiss();
    }

    @Override
    public void showCountDownTimer() {
        Log.d(TAG, "showCountDownTimer: ");
        mCountDownTimer.start();
    }

    @Override
    public void showSmsCodeCheckState(boolean b) {
        Log.d(TAG, "showSmsCodeCheckState: " + b);
        if (!b) {
            //提示验证码错误
            mErrorView.setVisibility(View.VISIBLE);
            mVerificationInput.setEnabled(true);
            mLoading.setVisibility(View.GONE);
        } else {

            mErrorView.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            // 检查用户是否存在
            mPresenter.requestCheckUserExist(mPhone);
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

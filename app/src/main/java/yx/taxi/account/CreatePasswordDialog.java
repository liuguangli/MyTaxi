package yx.taxi.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yangxiong.mytaxi.R;

import yx.taxi.account.model.AccountManagerImpl;
import yx.taxi.account.presenter.CreatePSWDialogPresenterImpl;
import yx.taxi.account.presenter.ICreatePSWDialogPresenter;
import yx.taxi.account.view.ICreatePSWDialogView;
import yx.taxi.common.http.IHttpClient;
import yx.taxi.common.http.impl.OkHttpClientImpl;
import yx.taxi.common.util.ToastUtil;

/**
 * Created by yangxiong on 2018/5/2/002.
 */

public class CreatePasswordDialog extends Dialog implements ICreatePSWDialogView {
    private static final String TAG = "CreatePasswordDialog";
    private TextView mTitle;
    private TextView mPhone;
    private EditText mPw;
    private EditText mPw1;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private IHttpClient mHttpClient;
    private String mPhoneStr;
    private ICreatePSWDialogPresenter mPresenter;

    @Override
    public void showRegisterSuc(boolean suc) {
        if (suc) showRegisterSuc( );
    }

    @Override
    public void loginSuc(boolean suc) {
        showLoginSuc( );
    }

    @Override
    public void showLoading() {
        showOrHideLoading(true);
    }

    @Override
    public void showError(int Code, String msg) {
        showServerError( );
    }

    public CreatePasswordDialog(Context context, String mPhone) {
        super(context);
        mPhoneStr = mPhone;
        mHttpClient = new OkHttpClientImpl( );
        mPresenter = new CreatePSWDialogPresenterImpl(
                this, new AccountManagerImpl(mHttpClient));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow( );

    }

    @Override
    public void dismiss() {
        super.dismiss( );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater( ).inflate(R.layout.dialog_create_pw, null);
        setContentView(view);
        initView( );
    }

    private void initView() {
        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.pw);
        mPw1 = (EditText) findViewById(R.id.pw1);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                dismiss( );
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                submit( );
            }
        });
        mPhone.setText(mPhoneStr);
    }

    private void submit() {
        if (checkPassword( )) {
            final String password = mPw.getText( ).toString( );
            final String phonePhone = mPhoneStr;
            // 请求网络， 提交注册
            mPresenter.submitRegister(phonePhone, password);
        }
    }

    /**
     * 处理注册成功
     */
    public void showRegisterSuc() {
        Log.d(TAG, "showRegisterSuc: ");
        mLoading.setVisibility(View.VISIBLE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext( )
                .getResources( )
                .getColor(R.color.color_text_normal));
        mTips.setText(getContext( )
                .getString(R.string.register_suc_and_loging));
        // 请求网络，完成自动登录

        mPresenter.login(mPhoneStr, mPw.getText( ).toString( ));
    }

    public void showLoginSuc() {
        Log.d(TAG, "showLoginSuc: ");
        dismiss( );
        ToastUtil.show(getContext( ), getContext( ).getString(R.string.login_suc));
    }

    public void showOrHideLoading(boolean show) {
        if (show) {
            mLoading.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.VISIBLE);
        }

    }

    public void showServerError() {
        Log.d(TAG, "showServerError: ");
        dismiss( );
        mTips.setTextColor(getContext( )
                .getResources( ).getColor(R.color.error_red));
        mTips.setText(getContext( ).getString(R.string.error_server));
    }

    private boolean checkPassword() {
        String password = mPw.getText( ).toString( );
        if (TextUtils.isEmpty(password)) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext( ).getString(R.string.password_is_null));
            mTips.setTextColor(getContext( ).
                    getResources( ).getColor(R.color.error_red));
            return false;
        }
        if (!password.equals(mPw1.getText( ).toString( ))) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext( )
                    .getString(R.string.password_is_not_equal));
            mTips.setTextColor(getContext( )
                    .getResources( ).getColor(R.color.error_red));
            return false;
        }
        return true;
    }
}

package yx.taxi.account.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

import yx.taxi.account.model.IAccountManager;
import yx.taxi.account.view.ISmsCodeDialogView;

/**
 * Created by yangxiong on 2018/5/3/003.
 */

public class SmsCodeDialogPresenterImpl implements ISmsCodeDialogPresenter {
    private static final String TAG = "SmsCodeDialogP";
    private ISmsCodeDialogView smsCodeDialogView;
    private IAccountManager accountManager;

    public SmsCodeDialogPresenterImpl(ISmsCodeDialogView smsCodeDialogView, IAccountManager accountManager){

        this.smsCodeDialogView = smsCodeDialogView;
        this.accountManager = accountManager;
        accountManager.setmHandler(new MyHandler(this));
    }
     static class MyHandler extends Handler {

         WeakReference<SmsCodeDialogPresenterImpl> refContext;
        public MyHandler(SmsCodeDialogPresenterImpl context) {
            refContext = new WeakReference(context);
        }

        @Override
        public void handleMessage(Message msg) {

            SmsCodeDialogPresenterImpl presenter = refContext.get();
            Log.d(TAG, "handleMessage: "+msg.what);
            switch (msg.what) {
                case IAccountManager.SMS_SEND_SUC:
                    presenter.smsCodeDialogView.showCountDownTimer();
                    break;
                case IAccountManager.SMS_SEND_FAIL:
                    presenter.smsCodeDialogView.showError(IAccountManager.SMS_SEND_FAIL, "");
                    break;
                case IAccountManager.SMS_CHECK_SUC:
                    presenter.smsCodeDialogView.showSmsCodeCheckState(true);
                    break;
                case IAccountManager.SMS_CHECK_FAIL:
                    presenter.smsCodeDialogView.showError(IAccountManager.SMS_CHECK_FAIL, "");
                    break;
                case IAccountManager.USER_EXIST:
                    presenter.smsCodeDialogView.showUserExist(true);
                    break;
                case IAccountManager.USER_NOT_EXIST:
                    presenter.smsCodeDialogView.showUserExist(false);
                    break;
                case IAccountManager.SERVER_FAIL:
                    presenter.smsCodeDialogView.showError(IAccountManager.SERVER_FAIL, "");
                    break;

            }
        }
    }
    @Override
    public void requestSendSmsCode(String phone) {
        accountManager.fetchSMSCode(phone);
    }

    @Override
    public void requestCheckSmsCode(String phone, String smsCode) {
        accountManager.checkSmsCode(phone,smsCode);
    }

    @Override
    public void requestCheckUserExist(String phone) {
        accountManager.checkUserExist(phone);
    }
}

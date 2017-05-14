package com.dalimao.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.dalimao.mytaxi.account.model.IAccountManager;
import com.dalimao.mytaxi.account.view.ISmsCodeDialogView;

import java.lang.ref.WeakReference;

/**
 * Created by liuguangli on 17/5/13.
 */

public class SmsCodeDialogPresenterImpl implements ISmsCodeDialogPresenter {
    private ISmsCodeDialogView view;
    private IAccountManager accountManager;
    /**
     * 接受消息并处理
     */
    private static class MyHandler extends Handler {
        WeakReference<SmsCodeDialogPresenterImpl> refContext;
        public MyHandler(SmsCodeDialogPresenterImpl context) {
            refContext = new WeakReference(context);
        }

        @Override
        public void handleMessage(Message msg) {

            SmsCodeDialogPresenterImpl presenter = refContext.get();
            switch (msg.what) {
                case IAccountManager.SMS_SEND_SUC:
                    presenter.view.showCountDownTimer();
                    break;
                case IAccountManager.SMS_SEND_FAIL:
                    presenter.view.showError(IAccountManager.SMS_SEND_FAIL, "");
                    break;
                case IAccountManager.SMS_CHECK_SUC:
                    presenter.view.showSmsCodeCheckState(true);

                    break;
                case IAccountManager.SMS_CHECK_FAIL:
                    presenter.view.showError(IAccountManager.SMS_CHECK_FAIL, "");
                    break;
                case IAccountManager.USER_EXIST:
                    presenter.view.showUserExist(true);
                    break;
                case IAccountManager.USER_NOT_EXIST:
                    presenter.view.showUserExist(false);
                    break;
                case IAccountManager.SERVER_FAIL:
                    presenter.view.showError(IAccountManager.SERVER_FAIL, "");
                    break;

            }
        }
    }
    public SmsCodeDialogPresenterImpl(ISmsCodeDialogView view,
                                      IAccountManager accountManager) {
        this.view = view;
        this.accountManager = accountManager;
        accountManager.setHandler(new MyHandler(this));
    }

    /**
     * 获取验证码
     * @param phone
     */
    @Override
    public void requestSendSmsCode(String phone) {
        accountManager.fetchSMSCode(phone);
    }

    /**
     * 验证码这验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void requestCheckSmsCode(String phone, String smsCode) {

        accountManager.checkSmsCode(phone, smsCode);
    }

    /**
     * 检查用户是否存在
     * @param phone
     */
    @Override
    public void requestCheckUserExist(String phone) {

        accountManager.checkUserExist(phone);
    }
}

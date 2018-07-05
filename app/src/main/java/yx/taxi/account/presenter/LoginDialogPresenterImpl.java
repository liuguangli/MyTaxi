package yx.taxi.account.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

import yx.taxi.account.model.IAccountManager;
import yx.taxi.account.view.ILoginView;

/**
 * Created by yangxiong on 2018/5/4/004.
 */

public class LoginDialogPresenterImpl implements ILoginDialogPresenter {
    private static final String TAG ="LoginPresenter" ;
    private ILoginView view;
    private IAccountManager accountManager;

    public LoginDialogPresenterImpl(ILoginView view, IAccountManager accountManager) {
        this.view = view;
        this.accountManager = accountManager;
        accountManager.setmHandler(new MyHandler(this));
    }

    @Override
    public void requestLogin(String mPhoneStr, String password) {
        accountManager.login(mPhoneStr,password);
    }
    static class MyHandler extends Handler {


        WeakReference<LoginDialogPresenterImpl> refContext;
        public MyHandler(LoginDialogPresenterImpl context) {
            refContext = new WeakReference(context);
        }

        @Override
        public void handleMessage(Message msg) {

            LoginDialogPresenterImpl presenter = refContext.get();
            Log.d(TAG, "handleMessage: "+msg.what);
            switch (msg.what) {
                case IAccountManager.LOGIN_SUC:
                    presenter.view.showLoginSuc();
                    break;

            }
        }
    }
}

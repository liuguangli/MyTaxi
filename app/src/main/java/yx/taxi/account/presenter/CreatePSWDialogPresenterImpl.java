package yx.taxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import yx.taxi.account.model.IAccountManager;
import yx.taxi.account.view.ICreatePSWDialogView;

/**
 * Created by yangxiong on 2018/5/4/004.
 */

public class CreatePSWDialogPresenterImpl implements ICreatePSWDialogPresenter {
    private ICreatePSWDialogView view;
    private IAccountManager manager;

    public CreatePSWDialogPresenterImpl(ICreatePSWDialogView view, IAccountManager manager){
        this.view = view;
        this.manager = manager;
        manager.setmHandler(new MyHandler(this));
    }
    @Override
    public void submitRegister(String phone, String name) {
        manager.register(phone,name);
    }

    @Override
    public void login(String phone,String psw) {
        manager.login(phone,psw);
    }

  static   class MyHandler extends Handler{
        WeakReference<CreatePSWDialogPresenterImpl> reference ;
        public MyHandler( CreatePSWDialogPresenterImpl presenter){
            reference = new WeakReference<CreatePSWDialogPresenterImpl>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CreatePSWDialogPresenterImpl presenter = reference.get( );
            if (presenter == null) return;
            switch (msg.what){
                case IAccountManager.REGISTER_SUC:
                    presenter.view.showRegisterSuc(true);
                    break;
                case IAccountManager.LOGIN_SUC:
                    presenter.view.loginSuc(true);
                    break;
                case IAccountManager.SERVER_FAIL:
                    presenter.view.showError(IAccountManager.PW_ERROR,"SERVER_FAIL");
                    break;
            }
        }
    }
}

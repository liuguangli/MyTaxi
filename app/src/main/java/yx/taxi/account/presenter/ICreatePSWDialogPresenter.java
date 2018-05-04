package yx.taxi.account.presenter;

/**
 * Created by yangxiong on 2018/5/4/004.
 */

public interface ICreatePSWDialogPresenter {
    void submitRegister(String phone , String name);
    void login(String phone,String psw);
}

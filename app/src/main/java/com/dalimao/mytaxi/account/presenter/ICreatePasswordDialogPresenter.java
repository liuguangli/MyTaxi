package com.dalimao.mytaxi.account.presenter;

/**
 * Created by liuguangli on 17/5/13.
 */

public interface ICreatePasswordDialogPresenter {
    /**
     * 校验密码输入合法性
     */
    void checkPw(String pw, String pw1);
    /**
     *  提交注册
     */
    void requestRegister(String phone, String pw);

    /**
     * 登录
     */
    void reqeustLogin(String phone, String pw);
}

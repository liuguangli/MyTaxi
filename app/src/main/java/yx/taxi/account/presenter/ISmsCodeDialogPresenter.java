package yx.taxi.account.presenter;

/**
 * Created by yangxiong on 2018/5/3/003.
 */

public interface ISmsCodeDialogPresenter {
    /**
     *  请求下发验证码
     */
    void requestSendSmsCode(String phone);
    /**
     * 请求校验验证码
     */
    void requestCheckSmsCode(String phone, String smsCode);
    /**
     * 用户是否存在
     */
    void requestCheckUserExist(String phone);
}

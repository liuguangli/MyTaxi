package com.dalimao.mytaxi.account.model;

/**
 * Created by liuguangli on 17/5/9.
 * 帐号相关的业务逻辑
 */

public interface IAccountManager {
    /**
     *  下发验证码
     */
    void fetchSMSCode(String phone);
    /**
     * 校验验证码
     */
    void checkSmsCode(String phone, String smsCode);
    /**
     *  用户是否注册接口
     */
    void checkUserExist(String phone);

    /**
     *  注册
     */
    void register(String phone, String password);

    /**
     *  登录
     */
    void login(String phone, String password);

    /**
     * token 登录
     */
    void loginByToken();
}

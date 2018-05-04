package yx.taxi.account.view;

/**
 * Created by yangxiong on 2018/5/3/003.
 */

public interface ISmsCodeDialogView extends IView {
    /**
     * 用户是否存在
     * @param b
     */
    void showUserExist(boolean b);

    /**
     * 显示倒计时
     */
    void showCountDownTimer();

    /**
     * 显示验证状态
     * @param b
     */
    void showSmsCodeCheckState(boolean b);
}

package com.dalimao.mytaxi.account.view;


/**
 * Created by liuguangli on 17/5/13.
 */

public interface ISmsCodeDialogView extends IView {


    /**
     * 显示倒计时
     */
    void showCountDownTimer();
    /**
     *  关闭视图
     */
    void close();
}

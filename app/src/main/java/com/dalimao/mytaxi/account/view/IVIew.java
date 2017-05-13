package com.dalimao.mytaxi.account.view;

/**
 * Created by liuguangli on 17/5/13.
 */

public interface IView {
    /**
     * 显示loading
     */
    void showLoading();
    /**
     *  显示错误
     */
    void showError(int Code, String msg);
}

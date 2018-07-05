package yx.taxi.account.view;

/**
 * Created by yangxiong on 2018/5/3/003.
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

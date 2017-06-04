package com.dalimao.mytaxi.main.presenter;

import android.os.Handler;
import android.os.Message;
import com.dalimao.mytaxi.account.model.IAccountManager;
import com.dalimao.mytaxi.account.model.response.LoginResponse;
import com.dalimao.mytaxi.common.databus.RegisterBus;
import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;
import com.dalimao.mytaxi.common.lbs.LocationInfo;
import com.dalimao.mytaxi.main.model.IMainManager;
import com.dalimao.mytaxi.main.model.bean.Order;
import com.dalimao.mytaxi.main.model.response.NearDriversResponse;
import com.dalimao.mytaxi.main.model.response.OrderStateOptResponse;
import com.dalimao.mytaxi.main.view.IMainView;

import java.lang.ref.WeakReference;

/**
 * Created by liuguangli on 17/5/14.
 */

public class MainPresenterImpl implements IMainPresenter {

    private IMainView view;
    private IAccountManager accountManager;
    private IMainManager mainManager;
    // 当前的订单
    private Order mCurrentOrder;

    /**
     * 接收子线程消息的 Handler
     */
    static class MyHandler extends Handler {

        // 弱引用
        WeakReference<MainPresenterImpl> dialogRef;
        public MyHandler(MainPresenterImpl presenter)
        {
            dialogRef = new WeakReference<MainPresenterImpl>(presenter);
        }
        @Override
        public void handleMessage(Message msg) {
            MainPresenterImpl presenter = dialogRef.get();
            if (presenter == null) {
                return;
            }
            // 处理UI 变化
            switch (msg.what) {
                case IAccountManager.LOGIN_SUC:
                    // 登录成功
                    presenter.view.showLoginSuc();
                    break;
                case IAccountManager.TOKEN_INVALID:
                    // 登录过期
                    presenter.view.showError(IAccountManager.TOKEN_INVALID, "");
                    break;
                case IAccountManager.SERVER_FAIL:
                    // 服务器错误
                    presenter.view.showError(IAccountManager.SERVER_FAIL, "");
                    break;
            }

        }
    }

    @RegisterBus
    public void onLoginResponse(LoginResponse loginResponse) {
        switch (loginResponse.getCode()) {


            case IAccountManager.LOGIN_SUC:
                // 登录成功
                view.showLoginSuc();
                break;
            case IAccountManager.TOKEN_INVALID:
                // 登录过期
                view.showError(IAccountManager.TOKEN_INVALID, "");
                break;
            case IAccountManager.SERVER_FAIL:
                // 服务器错误
                view.showError(IAccountManager.SERVER_FAIL, "");
                break;
        }
    }
    @RegisterBus
    public void onNearDriversResponse(NearDriversResponse response){
        if (response.getCode() == BaseBizResponse.STATE_OK) {
            view.showNears(response.getData());
        }
    }

    @RegisterBus
   public void onLocationInfo(LocationInfo locationInfo) {

        view.showLocationChange(locationInfo);
    }
    // todo 订单状态响应
    @RegisterBus
    public void onOrderOptResponse(OrderStateOptResponse response) {
        if (response.getState() == OrderStateOptResponse.ORDER_STATE_CREATE) {
            // 呼叫司机
            if (response.getCode() == BaseBizResponse.STATE_OK) {
                view.showCallDriverSuc();
                // 保存当前的订单
                mCurrentOrder = response.getData();
            } else {
                view.showCallDriverFail();
            }
        } else if (response.getState() == OrderStateOptResponse.ORDER_STATE_CANCEL) {
            // 取消订单
            if (response.getCode() == BaseBizResponse.STATE_OK) {
                mCurrentOrder = null;
                view.showCancelSuc();

            } else {
                view.showCancelFail();
            }
        } else if (response.getState() == OrderStateOptResponse.ORDER_STATE_ACCEPT) {
            // 司机接单
            mCurrentOrder = response.getData();
            view.showDriverAcceptOrder(mCurrentOrder);
        }
    }


    public MainPresenterImpl(IMainView view,
                             IAccountManager accountManager,
                             IMainManager mainManager) {
        this.view = view;
        this.accountManager = accountManager;
        this.mainManager = mainManager;

    }

    @Override
    public void loginByToken() {
        accountManager.loginByToken();
    }

    @Override
    public void fetchNearDrivers(double latitude, double longitude) {

        mainManager.fetchNearDrivers(latitude, longitude);

    }

    @Override
    public void updateLocationToServer(LocationInfo locationInfo) {
        mainManager.updateLocationToServer(locationInfo);
    }

    @Override
    public void callDriver(String key, float cost, LocationInfo startLocation, LocationInfo endLocation) {
        mainManager.callDriver(key, cost, startLocation, endLocation);
    }

    @Override
    public boolean isLogin() {
        return accountManager.isLogin();
    }

    /**
     *  取消呼叫
     */
    @Override
    public void cancel() {
        if (mCurrentOrder != null) {
            mainManager.cancelOrder(mCurrentOrder.getOrderId());
        } else {
            view.showCancelSuc();
        }
    }

}

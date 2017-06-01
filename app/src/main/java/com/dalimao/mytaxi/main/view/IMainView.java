package com.dalimao.mytaxi.main.view;

import com.dalimao.mytaxi.account.view.IView;
import com.dalimao.mytaxi.common.lbs.LocationInfo;

import java.util.List;

/**
 * Created by liuguangli on 17/5/14.
 */

public interface IMainView extends IView {
    void showLoginSuc();

    /**
     * 附近司机
     * @param data
     */
    void showNears(List<LocationInfo> data);
}

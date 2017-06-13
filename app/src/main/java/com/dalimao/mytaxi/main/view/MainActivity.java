package com.dalimao.mytaxi.main.view;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.dalimao.mytaxi.MyTaxiApplication;
import com.dalimao.mytaxi.R;
import com.dalimao.mytaxi.account.model.AccountManagerImpl;
import com.dalimao.mytaxi.account.model.IAccountManager;
import com.dalimao.mytaxi.account.view.PhoneInputDialog;

import com.dalimao.mytaxi.common.databus.RxBus;
import com.dalimao.mytaxi.common.http.IHttpClient;
import com.dalimao.mytaxi.common.http.impl.OkHttpClientImpl;
import com.dalimao.mytaxi.common.lbs.GaodeLbsLayerImpl;
import com.dalimao.mytaxi.common.lbs.ILbsLayer;
import com.dalimao.mytaxi.common.lbs.LocationInfo;
import com.dalimao.mytaxi.common.storage.SharedPreferencesDao;
import com.dalimao.mytaxi.common.util.SensorEventHelper;
import com.dalimao.mytaxi.common.util.ToastUtil;
import com.dalimao.mytaxi.main.presenter.IMainPresenter;
import com.dalimao.mytaxi.main.presenter.MainPresenterImpl;



/** －－－ 登录逻辑－－－
 *  1 检查本地纪录(登录态检查)
 *  2 若用户没登录则登录
 *  3 登录之前先校验手机号码
 *  4 token 有效使用 token 自动登录
 *  －－－－ 地图初始化－－－
 *  1 地图接入
 *  2 定位自己的位置，显示蓝点
 *  3 使用 Marker 标记当前位置和方向
 *
 */
public class MainActivity extends AppCompatActivity
        implements  IMainView {
    private final static String TAG = "MainActivity";
    private IMainPresenter mPresenter;
    private ILbsLayer mLbsLayer;
    private SensorEventHelper mSensorHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IHttpClient httpClient =  new OkHttpClientImpl();
        SharedPreferencesDao dao =
                new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        IAccountManager manager = new AccountManagerImpl(httpClient, dao);
        mPresenter = new MainPresenterImpl(this, manager);
        RxBus.getInstance().register(mPresenter);
        mPresenter.loginByToken();

        // 地图服务
        mLbsLayer = new GaodeLbsLayerImpl(this);
        mLbsLayer.onCreate(savedInstanceState);
        mLbsLayer.setLocationChangeListener(new ILbsLayer.CommonLocationChangeListener() {
            @Override
            public void onLocationChanged(LocationInfo locationInfo) {

            }

            @Override
            public void onLocation(LocationInfo locationInfo) {
                // 首次定位，添加当前位置的标记
                mLbsLayer.addOrUpdateMarker(locationInfo, BitmapFactory.decodeResource(getResources(), R.drawable.navi_map_gps_locked));
            }
        });
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.activity_main);
        mapViewContainer.addView(mLbsLayer.getMapView());

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mLbsLayer.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mLbsLayer.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLbsLayer.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(mPresenter);
        mLbsLayer.onDestroy();
    }

    /**
     *  自动登录成功
     */

    @Override
    public void showLoginSuc() {
        ToastUtil.show(this, getString(R.string.login_suc));
    }

    /**
     * 显示 loading
     */
    @Override
    public void showLoading() {
       // TODO: 17/5/14   显示加载框
    }
    /**
     * 错误处理
     * @param code
     * @param msg
     */

    @Override
    public void showError(int code, String msg) {
        switch (code) {
            case IAccountManager.TOKEN_INVALID:
                // 登录过期
                ToastUtil.show(this, getString(R.string.token_invalid));
                showPhoneInputDialog();
                break;
            case IAccountManager.SERVER_FAIL:
                // 服务器错误
                showPhoneInputDialog();
                break;

        }
    }

    /**
     * 显示手机输入框
     */
    private void showPhoneInputDialog() {
        PhoneInputDialog dialog = new PhoneInputDialog(this);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                RxBus.getInstance().register(mPresenter);
            }
        });
        RxBus.getInstance().unRegister(mPresenter);
    }
}

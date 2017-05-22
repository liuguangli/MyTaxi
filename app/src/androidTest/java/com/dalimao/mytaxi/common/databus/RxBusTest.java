package com.dalimao.mytaxi.common.databus;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.functions.Func1;

import static org.junit.Assert.*;

/**
 * Created by liuguangli on 17/5/17.
 */
public class RxBusTest {
    public static final String TAG = "RxBusTest";
    Presenter presenter;

    @Before
    public void setUp() throws Exception {
        /**
         *  初始化 presenter 并注册
         */
        presenter = new Presenter(new Manager());
        RxBus.getInstance().register(presenter);
    }

    @After
    public void tearDown() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RxBus.getInstance().unRegister(presenter);
    }

    @Test
    public void testGetUser() throws Exception {

        presenter.getUser();
    }

    @Test
    public void testGetOrder() throws Exception {
        presenter.getOrder();
    }

}


/**
 *  模拟 Presenter
 */
class Presenter  {
    private Manager manager;

    public Presenter(Manager manager) {
        this.manager = manager;
    }

    public void getUser() {
        manager.getUser();
    }

    public void getOrder() {
        manager.getOrder();
    }



    @RegisterBus
    public void onUser(User user) {
        Log.d(RxBusTest.TAG , "receive User in thread:" + Thread.currentThread());
    }

    @RegisterBus
    public void onOrder(Order order) {
        Log.d(RxBusTest.TAG , "receive order in thread:" + Thread.currentThread());
    }

}

/**
 *  模拟 MODEL,
 */
class Manager {
    public void getUser () {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                Log.d(RxBusTest.TAG, "chainProcess getUser start in thread:" + Thread.currentThread());
                User user = new User();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 把 User 数据到 Presenter
                return user;
            }
        });
    }

    public void getOrder() {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                Log.d(RxBusTest.TAG, "chainProcess getOrder start in thread::" + Thread.currentThread());
                Order order = new Order();

                // 把 order 数据到 Presenter
                return order;
            }
        });
    }
}

/**
 *  要返回的数据类型
 */

class User {

}

class Order {

}
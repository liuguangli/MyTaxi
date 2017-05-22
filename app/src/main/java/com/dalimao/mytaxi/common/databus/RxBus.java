package com.dalimao.mytaxi.common.databus;

import android.util.Log;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;



/**
 * Created by liuguangli on 17/5/17.
 */

public class RxBus {
    private static final String TAG = "RxBus";
    private static volatile RxBus instance;
    // 订阅者集合
    private Set<DataBusSubscriber> subscribers;

    /**
     *  注册 DataBusSubscriber
     * @param subscriber
     */
    public synchronized void register(DataBusSubscriber subscriber) {
        subscribers.add(subscriber);
    }


    /**
     *  注销 DataBusSubscriber
     * @param subscriber
     */
    public synchronized void unRegister(DataBusSubscriber subscriber) {
        subscribers.remove(subscriber);
    }


    /**
     *  单利模式
     */
    private RxBus() {
        subscribers = new CopyOnWriteArraySet<>();
    }
    public static synchronized RxBus getInstance() {

        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }

            }
        }
        return instance;
    }

    /**
     * 包装处理过程
     * @param func
     */
    public void chainProcess(Func1 func) {
        Observable.just("")
                .subscribeOn(Schedulers.io()) // 指定处理过程在 IO 线程
                .map(func)   // 包装处理过程
                .observeOn(AndroidSchedulers.mainThread())  // 指定事件消费在 Main 线程
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object data) {
                        Log.d(TAG, "chainProcess start");
                        for (DataBusSubscriber s : subscribers) {
                            // 数据发送到注册的 DataBusSubscriber
                            s.onEvent(data);
                        }

                    }
                });
    }






}

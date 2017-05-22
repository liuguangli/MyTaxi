package com.dalimao.mytaxi;

/**
 * Created by liuguangli on 17/3/5.
 */


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@RunWith(AndroidJUnit4.class)
public class TestRxJavaInAndroid {
    // map
    @Test
    public void testMapInAndroid() {
        Observable.just("dalimao")
                .subscribeOn(Schedulers.io()) // 指定下一个产生的线程节点在 IO 线程中处理
                .map(new Func1<String, User>() {
                    @Override
                    public User call(String name) {
                        User user = new User();
                        user.setName(name);
                        System.out.println("process User call in tread:" +
                                Thread.currentThread().getName());
                        return user;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // 指定消费节点在 Main 线程
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object data) {

                        System.out.println("receive User call in tread:"
                                + Thread.currentThread().getName());
                    }
                });
    }
   public static class User {
        String name;

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }
   }

}

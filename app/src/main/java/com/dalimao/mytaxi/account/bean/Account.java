package com.dalimao.mytaxi.account.bean;

/**
 * Created by liuguangli on 17/5/1.
 */

public class Account {
    private String account;
    private String token;
    private String uid;
    private long expired;
    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTokent() {
        return token;
    }

    public void setTokent(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

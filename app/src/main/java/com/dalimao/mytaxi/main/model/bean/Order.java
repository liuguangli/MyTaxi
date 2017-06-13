package com.dalimao.mytaxi.main.model.bean;

/**
 * Created by liuguangli
 */
public class Order {
    private float cost;
    private int state;
    private String orderId;
    private String uid;
    private String key;
    private String phone;
    private double driverLatitude;
    private double driverLongitude;
    private double endLatitude;
    private double endLongitude;
    private double driverRotation;
    private String driverName;
    private String carNo;
    private String driverPhone;
    private String startAddr;
    private String endAddr;

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(double driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public double getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(double driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public double getDriverRotation() {
        return driverRotation;
    }

    public void setDriverRotation(double driverRotation) {
        this.driverRotation = driverRotation;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }

    public String getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(String endAddr) {
        this.endAddr = endAddr;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }
}

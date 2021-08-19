package com.ark.robokart_robotics.Activities.shop;

public class CouponItem {

    String code,detail;
    int percent,minimum;

    public CouponItem(String code,String detail, int percent, int minimum) {
        this.code=code;
        this.detail=detail;
        this.percent=percent;
        this.minimum=minimum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getPercent() {
        return percent;
    }

    public String getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }
}

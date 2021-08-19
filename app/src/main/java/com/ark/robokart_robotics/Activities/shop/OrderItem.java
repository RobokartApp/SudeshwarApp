package com.ark.robokart_robotics.Activities.shop;

public class OrderItem {

    String img,name,delivery,order_id;

    public OrderItem(String order_id,String img, String name, String delivery) {
        this.img =img;
        this.name=name;
        this.delivery=delivery;
        this.order_id=order_id;
    }

    public String getDelivery() {
        return delivery;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getOrder_id() {
        return order_id;
    }
}

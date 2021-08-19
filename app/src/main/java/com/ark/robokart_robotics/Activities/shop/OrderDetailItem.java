package com.ark.robokart_robotics.Activities.shop;

public class OrderDetailItem {

    private String images;
    private String mrp;
    private String name;
    private String offer_price;int qty;

    public OrderDetailItem(String images, String name, String offer_price, String mrp,int qty) {

        this.images = images;
        this.name = name;
        this.offer_price = offer_price;
        this.mrp = mrp;
        this.qty=qty;
    }

    public String getImages() {
        return this.images;
    }

    public String getMrp() {
        return this.mrp;
    }

    public String getName() {
        return this.name;
    }

    public String getOffer_price() {
        return this.offer_price;
    }

    public int getQty() {
        return qty;
    }
}

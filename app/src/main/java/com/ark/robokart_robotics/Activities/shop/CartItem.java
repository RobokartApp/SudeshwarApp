package com.ark.robokart_robotics.Activities.shop;

public class CartItem {

    /* renamed from: id */
    int f6id;
    private String images;
    private String mrp;
    private String name;
    private String offer_price;
    int qty;

    public CartItem(int id, String images2, String name2, String offer_price2, String mrp2, int qty2) {
        this.f6id = id;
        this.images = images2;
        this.name = name2;
        this.offer_price = offer_price2;
        this.mrp = mrp2;
        this.qty = qty2;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty2) {
        this.qty = qty2;
    }

    public int getId() {
        return this.f6id;
    }

    public void setId(int id) {
        this.f6id = id;
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

    public void setImages(String images2) {
        this.images = images2;
    }

    public void setMrp(String mrp2) {
        this.mrp = mrp2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public void setOffer_price(String offer_price2) {
        this.offer_price = offer_price2;
    }
}

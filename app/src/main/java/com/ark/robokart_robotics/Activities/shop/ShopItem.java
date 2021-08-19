package com.ark.robokart_robotics.Activities.shop;

public class ShopItem {
    private String category;
    private String description;
    private String gst;

    /* renamed from: id */
    int f8id;
    private String images;
    private String mrp;
    private String name;
    private String offer_price;
    private String subcategory;
    private String tags;

    public ShopItem(int id, String images2, String name2, String offer_price2, String mrp2, String gst2, String description2, String category2, String subcategory2, String tags2) {
        this.f8id = id;
        this.category = category2;
        this.description = description2;
        this.images = images2;
        this.name = name2;
        this.offer_price = offer_price2;
        this.mrp = mrp2;
        this.gst = gst2;
        this.subcategory = subcategory2;
        this.tags = tags2;
    }

    public ShopItem(String images, String name) {
        this.images = images;
        this.name = name;
    }

    public int getId() {
        return this.f8id;
    }

    public void setId(int id) {
        this.f8id = id;
    }

    public String getImages() {
        return this.images;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public String getGst() {
        return this.gst;
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

    public String getSubcategory() {
        return this.subcategory;
    }

    public String getTags() {
        return this.tags;
    }

    public void setCategory(String category2) {
        this.category = category2;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public void setGst(String gst2) {
        this.gst = gst2;
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

    public void setSubcategory(String subcategory2) {
        this.subcategory = subcategory2;
    }

    public void setTags(String tags2) {
        this.tags = tags2;
    }
}

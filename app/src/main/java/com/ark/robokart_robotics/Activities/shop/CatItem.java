package com.ark.robokart_robotics.Activities.shop;

public class CatItem {
    String cat_img;
    String cat_name;

    public CatItem(String cat_img2, String cat_name2) {
        this.cat_img = cat_img2;
        this.cat_name = cat_name2;
    }

    public String getCat_img() {
        return this.cat_img;
    }

    public String getCat_name() {
        return this.cat_name;
    }

    public void setCat_img(String cat_img2) {
        this.cat_img = cat_img2;
    }

    public void setCat_name(String cat_name2) {
        this.cat_name = cat_name2;
    }
}

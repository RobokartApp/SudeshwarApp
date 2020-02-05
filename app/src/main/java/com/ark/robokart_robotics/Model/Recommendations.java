package com.ark.robokart_robotics.Model;

public class Recommendations {

    private String r_id;
    private String r_name;


    public Recommendations(String r_id, String r_name) {
        this.r_id = r_id;
        this.r_name = r_name;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

}

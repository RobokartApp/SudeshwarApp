package com.ark.robokart_robotics.Fragments.profile;

public class DoubtItem {
    String url,doubt,id;
    public DoubtItem(String id,String url,String doubt){
        this.url=url;
        this.id=id;
        this.doubt=doubt;
    }

    public String getId() {
        return id;
    }

    public String getDoubt() {
        return doubt;
    }

    public String getUrl() {
        return url;
    }
}

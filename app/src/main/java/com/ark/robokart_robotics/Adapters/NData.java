package com.ark.robokart_robotics.Adapters;

public class NData {
private String title,body,time;
    public NData(String title, String body, String time){
this.body=body;this.title=title;this.time=time;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

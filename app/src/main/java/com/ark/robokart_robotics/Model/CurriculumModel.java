package com.ark.robokart_robotics.Model;

public class CurriculumModel {

    private String curr_name;
    private String curr_download_link;

    public CurriculumModel(String curr_name, String curr_download_link) {
        this.curr_name = curr_name;
        this.curr_download_link = curr_download_link;
    }

    public String getCurr_name() {
        return curr_name;
    }

    public void setCurr_name(String curr_name) {
        this.curr_name = curr_name;
    }

    public String getCurr_download_link() {
        return curr_download_link;
    }

    public void setCurr_download_link(String curr_download_link) {
        this.curr_download_link = curr_download_link;
    }
}

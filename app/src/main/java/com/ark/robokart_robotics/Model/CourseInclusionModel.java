package com.ark.robokart_robotics.Model;

public class CourseInclusionModel {

   private int num;
   private String chapter_name;


    public CourseInclusionModel(int num, String chapter_name) {
        this.num = num;
        this.chapter_name = chapter_name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }
}

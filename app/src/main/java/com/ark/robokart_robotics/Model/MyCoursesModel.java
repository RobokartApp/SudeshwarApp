package com.ark.robokart_robotics.Model;

public class MyCoursesModel {

    private String course_name;
    private String course_video_thumb;
    private String learn_percent;

    public MyCoursesModel(String course_name, String course_video_thumb, String learn_percent) {
        this.course_name = course_name;
        this.course_video_thumb = course_video_thumb;
        this.learn_percent = learn_percent;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_video_thumb() {
        return course_video_thumb;
    }

    public void setCourse_video_thumb(String course_video_thumb) {
        this.course_video_thumb = course_video_thumb;
    }

    public String getLearn_percent() {
        return learn_percent;
    }

    public void setLearn_percent(String learn_percent) {
        this.learn_percent = learn_percent;
    }
}

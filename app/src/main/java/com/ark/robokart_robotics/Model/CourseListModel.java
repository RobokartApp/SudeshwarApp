package com.ark.robokart_robotics.Model;

public class CourseListModel {

    private String course_id;
    private String course_level;
    private String course_name;
    private String customer_rating;
    private String course_header_imge;
    private String course_enrolled;

    public CourseListModel(String course_id, String course_level, String course_name, String customer_rating, String course_header_imge, String course_enrolled) {
        this.course_id = course_id;
        this.course_level = course_level;
        this.course_name = course_name;
        this.customer_rating = customer_rating;
        this.course_header_imge = course_header_imge;
        this.course_enrolled = course_enrolled;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_level() {
        return course_level;
    }

    public void setCourse_level(String course_level) {
        this.course_level = course_level;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(String customer_rating) {
        this.customer_rating = customer_rating;
    }

    public String getCourse_header_imge() {
        return course_header_imge;
    }

    public void setCourse_header_imge(String course_header_imge) {
        this.course_header_imge = course_header_imge;
    }

    public String getCourse_enrolled() {
        return course_enrolled;
    }

    public void setCourse_enrolled(String course_enrolled) {
        this.course_enrolled = course_enrolled;
    }
}

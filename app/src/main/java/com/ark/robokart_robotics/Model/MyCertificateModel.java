package com.ark.robokart_robotics.Model;

public class MyCertificateModel {

    private String course_date,result_id;
    private String course_name,cert_number;


    public MyCertificateModel( String course_name,String course_date,String cert_number,String result_id) {
        this.course_date = course_date;
        this.course_name = course_name;
        this.cert_number=cert_number;
        this.result_id=result_id;
    }

    public String getCourse_date() {
        return course_date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCert_number() {
        return cert_number;
    }

    public String getResult_id() {
        return result_id;
    }

    public void setResult_id(String result_id) {
        this.result_id = result_id;
    }

    public void setCert_number(String cert_number) {
        this.cert_number = cert_number;
    }

    public void setCourse_date(String course_date) {
        this.course_date = course_date;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}


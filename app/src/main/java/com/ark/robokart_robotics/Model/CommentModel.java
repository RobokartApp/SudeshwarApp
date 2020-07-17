package com.ark.robokart_robotics.Model;

public class CommentModel {

    private String comment_id;
    private String customer_name;
    private String customer_image;
    private String comment;

    public CommentModel(String comment_id, String customer_name, String customer_image, String comment) {
        this.comment_id = comment_id;
        this.customer_name = customer_name;
        this.customer_image = customer_image;
        this.comment = comment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_image() {
        return customer_image;
    }

    public void setCustomer_image(String customer_image) {
        this.customer_image = customer_image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

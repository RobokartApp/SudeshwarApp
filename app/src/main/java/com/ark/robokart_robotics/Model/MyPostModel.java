package com.ark.robokart_robotics.Model;

public class MyPostModel {
    private String post_id,post_img,post_title,post_like,post_comment,
            post_share,post_profile_img,post_profile_name,isLiked,post_date,by_user;

    public MyPostModel(String post_id,String post_img,String post_title,String post_like,
                       String post_comment,String post_share,String post_profile_img,
                       String post_profile_name,String isLiked,String post_date,String by_user) {
       this.post_comment=post_comment;
       this.by_user=by_user;
       this.post_id=post_id;
       this.post_img=post_img;
       this.post_like=post_like;
       this.post_share=post_share;
       this.post_title=post_title;
       this.post_profile_img=post_profile_img;
       this.post_profile_name=post_profile_name;
       this.isLiked=isLiked;
       this.post_date=post_date;
    }

    public String getBy_user() {
        return by_user;
    }

    public String getPost_comment() {
        return post_comment;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getPost_img() {
        return post_img;
    }

    public String getPost_like() {
        return post_like;
    }

    public String getPost_share() {
        return post_share;
    }

    public String getPost_title() {
        return post_title;
    }

    public String getPost_profile_img() {
        return post_profile_img;
    }

    public String getPost_profile_name() {
        return post_profile_name;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setBy_user(String by_user) {
        this.by_user = by_user;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public void setPost_profile_img(String post_profile_img) {
        this.post_profile_img = post_profile_img;
    }

    public void setPost_profile_name(String post_profile_name) {
        this.post_profile_name = post_profile_name;
    }

    public void setPost_comment(String post_comment) {
        this.post_comment = post_comment;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    public void setPost_like(String post_like) {
        this.post_like = post_like;
    }

    public void setPost_share(String post_share) {
        this.post_share = post_share;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }
}

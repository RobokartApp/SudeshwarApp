package com.ark.robokart_robotics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//public class ImageModel {
//
//    @Expose
//    @SerializedName("User_id")
//    private String User_id;
//
//    @Expose
//    @SerializedName("url_img")
//    private String url_img;
//
//    public String getCustomer_id() {
//        return User_id;
//    }
//
//    public void setCustomer_id(String User_id) {
//        this.User_id = User_id;
//    }
//
//    public String getUrl_img() {
//        return url_img;
//    }
//
//    public void setUrl_img(String url_img) {
//        this.url_img = url_img;
//    }
//}

public class ImageModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusId")
    @Expose
    private Integer statusId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public class Result {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("url_img")
        @Expose
        private String urlImg;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUrlImg() {
            return urlImg;
        }

        public void setUrlImg(String urlImg) {
            this.urlImg = urlImg;
        }

    }
}



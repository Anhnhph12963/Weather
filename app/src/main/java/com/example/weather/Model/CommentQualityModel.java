package com.example.weather.Model;

public class CommentQualityModel {
    private String tv_username, tv_comment,tv_date_comment;

    public CommentQualityModel() {
    }

    public CommentQualityModel(String tv_username, String tv_comment, String tv_date_comment) {
        this.tv_username = tv_username;
        this.tv_comment = tv_comment;
        this.tv_date_comment = tv_date_comment;
    }

    public String getTv_username() {
        return tv_username;
    }

    public void setTv_username(String tv_username) {
        this.tv_username = tv_username;
    }

    public String getTv_comment() {
        return tv_comment;
    }

    public void setTv_comment(String tv_comment) {
        this.tv_comment = tv_comment;
    }

    public String getTv_date_comment() {
        return tv_date_comment;
    }

    public void setTv_date_comment(String tv_date_comment) {
        this.tv_date_comment = tv_date_comment;
    }
}

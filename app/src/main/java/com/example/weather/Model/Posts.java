package com.example.weather.Model;

public class Posts {
    private String datePost, postImage, postTitle, userProfile, userName;

    public Posts() {
    }

    public Posts(String datePost, String postImage, String postTitle, String userProfile, String userName) {
        this.datePost = datePost;
        this.postImage = postImage;
        this.postTitle = postTitle;
        this.userProfile = userProfile;
        this.userName = userName;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }
}

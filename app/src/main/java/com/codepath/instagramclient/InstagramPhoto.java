package com.codepath.instagramclient;

public class InstagramPhoto {
    private String userName, caption, imageUrl;
    private int imageHeight, likesCount;

    public InstagramPhoto(String userName, String caption, String imageUrl, int imageHeight, int likesCount) {
        this.userName = userName;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
        this.likesCount = likesCount;
    }

    public String getUserName() {
        return userName;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return  imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getLikesCount() {
        return likesCount;
    }
}

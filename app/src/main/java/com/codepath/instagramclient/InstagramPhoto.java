package com.codepath.instagramclient;

public class InstagramPhoto {
    private String userName, userProfilePicUrl, caption, imageUrl;
    private int imageHeight, likesCount;
    private long createdTime;

    public InstagramPhoto(
            String userName, String userProfilePicUrl, String caption, String imageUrl,
            int imageHeight, int likesCount,
            long createdTime) {
        this.userName = userName;
        this.userProfilePicUrl = userProfilePicUrl;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
        this.likesCount = likesCount;
        this.createdTime = createdTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfilePicUrl() {
        return userProfilePicUrl;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public long getCreatedTime() {
        return createdTime;
    }
}

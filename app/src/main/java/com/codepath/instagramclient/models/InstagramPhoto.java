package com.codepath.instagramclient.models;

import java.util.ArrayList;
import java.util.List;

public class InstagramPhoto {
    private String userName, userProfilePicUrl, caption, imageUrl;
    private int imageHeight, likesCount, commentsCount;
    private long createdTime;
    private List<InstagramPhotoComment> comments;

    public InstagramPhoto(
            String userName, String userProfilePicUrl, String caption, String imageUrl,
            int imageHeight, int likesCount,
            long createdTime) {
        this(userName, userProfilePicUrl, caption, imageUrl,
                imageHeight, likesCount, 0,
                createdTime,
                new ArrayList<InstagramPhotoComment>());
    }

    public InstagramPhoto(
            String userName, String userProfilePicUrl, String caption, String imageUrl,
            int imageHeight, int likesCount, int commentsCount,
            long createdTime,
            List<InstagramPhotoComment> comments) {
        this.userName = userName;
        this.userProfilePicUrl = userProfilePicUrl;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
        this.likesCount = likesCount;
        this.createdTime = createdTime;
        this.comments = comments;
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

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public List<InstagramPhotoComment> getComments() {
        return comments;
    }

    public void setComments(List<InstagramPhotoComment> comments) {
        this.comments = comments;
    }
}

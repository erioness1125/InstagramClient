package com.codepath.instagramclient.models;

public class InstagramPhotoComment {
    private String userName, text;
    private long createdTime;

    public InstagramPhotoComment(String userName, String text, long createdTime) {
        this.userName = userName;
        this.text = text;
        this.createdTime = createdTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public long getCreatedTime() {
        return createdTime;
    }
}

package com.yesapp.yesapp.classes;

public class Friend {

    public Friend(){}

    private String friend_uid, friend_name, friend_status, profilePhotoLink;

    public String getFriend_uid() {
        return friend_uid;
    }

    public void setFriend_uid(String friend_uid) {
        this.friend_uid = friend_uid;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_status() {
        return friend_status;
    }

    public void setFriend_status(String friend_status) {
        this.friend_status = friend_status;
    }

    public String getProfilePhotoLink() {
        return profilePhotoLink;
    }

    public void setProfilePhotoLink(String profilePhotoLink) {
        this.profilePhotoLink = profilePhotoLink;
    }
}

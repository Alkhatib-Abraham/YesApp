package com.yesapp.yesapp;

public class User {

    public String username;
    public String message;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String message) {
        this.username = username;
        this.message = message;
    }
}

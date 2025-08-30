package com.example.capstoneproject.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Users {
    private String email;
    private String role;
    private String userID;
    private String username;
    private String phoneNumber;

    @ServerTimestamp
    private Timestamp creationDate;

    public Users() {

    }

    public Users(String userID, String email, String role, String username, String phoneNumber) {
        this.userID = userID;
        this.email = email;
        this.role = role;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
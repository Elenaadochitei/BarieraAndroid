package com.example.myapplication;

import retrofit2.Response;

public class LoginInfo {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;


    public boolean isUserActive() {
        return userActive;
    }

    public void setUserActive(boolean userActive) {
        this.userActive = userActive;
    }

    boolean userActive;

}
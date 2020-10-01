package com.example.myapplication;

import java.time.LocalDateTime;

public class NameAndPlateRegister {

    String userID;
    String name;
    String plateRegister;
    LocalDateTime expirationDate;

    public NameAndPlateRegister() {
    }

    public NameAndPlateRegister(String userID, String name, String plateRegister, LocalDateTime expirationDate) {
        this.userID = userID;
        this.name = name;
        this.plateRegister = plateRegister;
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlateRegister() {
        return plateRegister;
    }

    public void setPlateRegister(String plateRegister) {
        this.plateRegister = plateRegister;
    }
}

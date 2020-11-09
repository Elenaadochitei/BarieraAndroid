package com.example.myapplication;

public class SharedParkingSpace {

    int spaceNumber;
    String description;
    String startDate;
    String expirationDate;

    public SharedParkingSpace(int spaceNumber, String startDate, String expirationDate, String description) {
        this.spaceNumber = spaceNumber;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.description = description;
    }
    public SharedParkingSpace() {
    }

    public int getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(int spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

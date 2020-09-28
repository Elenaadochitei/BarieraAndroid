package com.example.myapplication;

public class Nume_Nr_Masina {

    String userID;
    String nume;
    String nrMasina;

    public Nume_Nr_Masina() {
    }

    public Nume_Nr_Masina(String userID, String nume, String nrMasina) {
        this.userID = userID;
        this.nume = nume;
        this.nrMasina = nrMasina;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNrMasina() {
        return nrMasina;
    }

    public void setNrMasina(String nrMasina) {
        this.nrMasina = nrMasina;
    }
}

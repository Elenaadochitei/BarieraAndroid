package com.example.myapplication;

public class Nume_Nr_Masina {

    String nume;
    String nrMasina;

    public Nume_Nr_Masina() {
    }

    public Nume_Nr_Masina(String nume, String nr_masina) {

        this.nume = nume;
        this.nrMasina = nr_masina;
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

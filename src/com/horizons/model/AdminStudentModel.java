package com.horizons.model;

public class AdminStudentModel {
    private int id;
    private int credentialID;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private int year;
    private String specialty;

    public AdminStudentModel(int id, int credentialID, String firstname, String lastname, String email,
                             String password, int year, String specialty) {
        this.id = id;
        this.credentialID = credentialID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.year = year;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(int credentialID) {
        this.credentialID = credentialID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}

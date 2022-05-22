package com.horizons.model;

/**
 * This is a data class to hold the data of supervisors in the AdminSupervisorController
 */
public class AdminSupervisorModel {

    private int id;
    private int credentialID;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public AdminSupervisorModel(int id, int credentialID, String firstname,
                                String lastname, String email, String password) {
        this.id = id;
        this.credentialID = credentialID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
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
}

package com.horizons.model;

/**
 * This is a data class to hold the data of professors in the SupervisorProfessorController
 */
public class SupervisorProfessorModel {
    private String firstname;
    private String lastname;
    private String subject;

    public SupervisorProfessorModel(String firstname, String lastname, String subject) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

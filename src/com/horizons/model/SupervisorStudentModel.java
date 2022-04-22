package com.horizons.model;

public class SupervisorStudentModel {
    private int id;
    private String firstname;
    private String lastname;
    private int year;
    private String specialty;

    public SupervisorStudentModel(int id, String firstname, String lastname,
                                  int year, String specialty) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.year = year;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

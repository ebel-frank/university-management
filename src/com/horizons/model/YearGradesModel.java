package com.horizons.model;

public class YearGradesModel {
    private String firstname;
    private String lastname;
    private double semester1;
    private double semester2;
    private String total;

    public YearGradesModel(String firstname, String lastname, double semester1,
                           double semester2, String total) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.semester1 = semester1;
        this.semester2 = semester2;
        this.total = total;
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

    public double getSemester1() {
        return semester1;
    }

    public void setSemester1(double semester1) {
        this.semester1 = semester1;
    }

    public double getSemester2() {
        return semester2;
    }

    public void setSemester2(double semester2) {
        this.semester2 = semester2;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

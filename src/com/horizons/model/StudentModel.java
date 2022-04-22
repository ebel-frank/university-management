package com.horizons.model;

public class StudentModel {
    private int id;
    private String fullname;
    private String specialty;
    private int examGrade;
    private int tpGrade;
    private int ccGrade;
    private double total;

    public StudentModel(int id, String fullname, String specialty, int examGrade,
                        int tpGrade, int ccGrade, int examCoeff, int tpCoeff, int ccCoeff) {
        this.id = id;
        this.fullname = fullname;
        this.specialty = specialty;
        this.examGrade = examGrade;
        this.tpGrade = tpGrade;
        this.ccGrade = ccGrade;
        this.total = ((examGrade * examCoeff * 0.01) + (tpGrade * tpCoeff * 0.01) + (ccGrade * ccCoeff * 0.01));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(int examGrade) {
        this.examGrade = examGrade;
    }

    public int getTpGrade() {
        return tpGrade;
    }

    public void setTpGrade(int tpGrade) {
        this.tpGrade = tpGrade;
    }

    public int getCcGrade() {
        return ccGrade;
    }

    public void setCcGrade(int ccGrade) {
        this.ccGrade = ccGrade;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

package com.horizons.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StudentModel {
    private int id;
    private SimpleStringProperty fullname;
    private SimpleStringProperty specialty;
    private SimpleIntegerProperty examGrade;
    private SimpleIntegerProperty tpGrade;
    private SimpleIntegerProperty ccGrade;
    private SimpleStringProperty total;

    public StudentModel(int id, String fullname, String specialty, int examGrade,
                        int tpGrade, int ccGrade, double total) {
        this.id = id;
        this.fullname = new SimpleStringProperty(fullname);
        this.specialty = new SimpleStringProperty(specialty);
        this.examGrade = new SimpleIntegerProperty(examGrade);
        this.tpGrade = new SimpleIntegerProperty(tpGrade);
        this.ccGrade = new SimpleIntegerProperty(ccGrade);
        this.total = new SimpleStringProperty(total+"");
    }

    public StudentModel(String title) {
        this.fullname = new SimpleStringProperty(title);
    }
    public StudentModel(String fullname, String specialty) {
        this.fullname = new SimpleStringProperty(fullname);
        this.specialty = new SimpleStringProperty(specialty);
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname.get();
    }

    public SimpleStringProperty fullnameProperty() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname.set(fullname);
    }

    public String getSpecialty() {
        return specialty.get();
    }

    public SimpleStringProperty specialtyProperty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty.set(specialty);
    }

    public int getExamGrade() {
        return examGrade.get();
    }

    public SimpleIntegerProperty examGradeProperty() {
        return examGrade;
    }

    public void setExamGrade(int examGrade) {
        this.examGrade.set(examGrade);
    }

    public int getTpGrade() {
        return tpGrade.get();
    }

    public SimpleIntegerProperty tpGradeProperty() {
        return tpGrade;
    }

    public void setTpGrade(int tpGrade) {
        this.tpGrade.set(tpGrade);
    }

    public int getCcGrade() {
        return ccGrade.get();
    }

    public SimpleIntegerProperty ccGradeProperty() {
        return ccGrade;
    }

    public void setCcGrade(int ccGrade) {
        this.ccGrade.set(ccGrade);
    }

    public String getTotal() {
        return total.get();
    }

    public SimpleStringProperty totalProperty() {
        return total;
    }

    public void setTotal(int examCoeff, int tpCoeff, int ccCoeff) {
        this.total.set((examGrade.get() * examCoeff * 0.01) + (tpGrade.get() * tpCoeff * 0.01) + (ccGrade.get() * ccCoeff * 0.01)+"");
    }
}

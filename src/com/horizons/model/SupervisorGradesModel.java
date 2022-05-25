package com.horizons.model;

/**
 * This is a data class to hold the data of student grades in the SupervisorGradesController
 */
public class SupervisorGradesModel {
    private int id;
    private String firstname;
    private String lastname;
    private int exam;
    private int tp;
    private int cc;
    private int examCoeff;
    private int tpCoeff;
    private int ccCoeff;
    private String total;
    private String situation;

    public SupervisorGradesModel(int id, String firstname, String lastname, int exam,
                                 int tp, int cc, int examCoeff, int tpCoeff, int ccCoeff, double total) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.exam = exam;
        this.tp = tp;
        this.cc = cc;
        this.examCoeff = examCoeff;
        this.tpCoeff = tpCoeff;
        this.ccCoeff = ccCoeff;
        this.total = total+"";
        if (total>=10) {
            this.situation = "R";
        } else {
            this.situation = "NR";
        }
    }

    public SupervisorGradesModel(String firstname, String lastname, double total) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.total = total+"";
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

    public int getExam() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam = exam;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal() {
        this.total = ""+((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01));
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}

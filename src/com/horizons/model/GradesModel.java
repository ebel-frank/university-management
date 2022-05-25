package com.horizons.model;

/**
 * This is a data class to hold the data of student grades in the ProfessorController
 */
public class GradesModel {
    private String subject;
    private String examGrade;
    private String tpGrade;
    private String ccGrade;
    private String totalGrade;

    public GradesModel(String subject,int examGrade,
                       int tpGrade, int ccGrade, int examCoeff,
                       int tpCoeff, int ccCoeff) {
        this.subject = subject;
        this.examGrade = examGrade+"";
        this.tpGrade = tpGrade+"";
        this.ccGrade = ccGrade+"";
        this.totalGrade = ((examGrade * examCoeff * 0.01) + (tpGrade * tpCoeff * 0.01) + (ccGrade * ccCoeff * 0.01))+"";
    }

    public GradesModel(String subject) {
        this.subject = subject;
        this.examGrade = "";
        this.tpGrade = "";
        this.ccGrade = "";
        this.totalGrade = "";
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(String examGrade) {
        this.examGrade = examGrade;
    }

    public String getTpGrade() {
        return tpGrade;
    }

    public void setTpGrade(String tpGrade) {
        this.tpGrade = tpGrade;
    }

    public String getCcGrade() {
        return ccGrade;
    }

    public void setCcGrade(String ccGrade) {
        this.ccGrade = ccGrade;
    }

    public String getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(String totalGrade) {
        this.totalGrade = totalGrade;
    }
}

package com.horizons.schoolmanagement.model;

public class SupervisorModuleModel {
    private String firstname;
    private String lastname;
    private float module1;
    private String module1Situation;
    private float module2;
    private String module2Situation;
    private float module3;
    private String module3Situation;
    private float semester;
    private String semesterSituation;

    public SupervisorModuleModel(String firstname, String lastname, float module1, float module2,
                                 float module3, float semester) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.module1 = module1;
        this.module1Situation = module1 >= 10 ? "V" : "R";
        this.module2 = module2;
        this.module2Situation = module2 >= 10 ? "V" : "R";
        this.module3 = module3;
        this.module3Situation = module3 >= 10 ? "V" : "R";
        this.semester = semester;
        this.semesterSituation = semester >= 10 ? "V" : "R";
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

    public float getModule1() {
        return module1;
    }

    public void setModule1(float module1) {
        this.module1 = module1;
    }

    public String getModule1Situation() {
        return module1Situation;
    }

    public void setModule1Situation(String module1Situation) {
        this.module1Situation = module1Situation;
    }

    public float getModule2() {
        return module2;
    }

    public void setModule2(float module2) {
        this.module2 = module2;
    }

    public String getModule2Situation() {
        return module2Situation;
    }

    public void setModule2Situation(String module2Situation) {
        this.module2Situation = module2Situation;
    }

    public float getModule3() {
        return module3;
    }

    public void setModule3(float module3) {
        this.module3 = module3;
    }

    public String getModule3Situation() {
        return module3Situation;
    }

    public void setModule3Situation(String module3Situation) {
        this.module3Situation = module3Situation;
    }

    public float getSemester() {
        return semester;
    }

    public void setSemester(float semester) {
        this.semester = semester;
    }

    public String getSemesterSituation() {
        return semesterSituation;
    }

    public void setSemesterSituation(String semesterSituation) {
        this.semesterSituation = semesterSituation;
    }
}

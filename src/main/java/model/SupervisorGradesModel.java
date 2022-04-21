package main.java.model;

public class SupervisorGradesModel {
    private int id;
    private String firstname;
    private String lastname;
    private String examGrade;
    private String tpGrade;
    private String ccGrade;
    private String totalGrade;
    private String situation;

    public SupervisorGradesModel(int id, String firstname, String lastname, double examGrade, double tpGrade, double ccGrade,
                                 double total) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.examGrade = examGrade+"";
        this.tpGrade = tpGrade+"";
        this.ccGrade = ccGrade+"";
        this.totalGrade = total+"";
        if (total>=10) {
            this.situation = "R";
        } else {
            this.situation = "NR";
        }
    }

    public SupervisorGradesModel(String firstname) {
        this.id = -1;
        this.firstname = firstname;
        this.lastname = "";
        this.examGrade = "";
        this.tpGrade = "";
        this.ccGrade = "";
        this.totalGrade = "";
        this.situation = "";
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

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

}

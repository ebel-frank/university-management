package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.SupervisorGradesModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.horizons.Utils.*;

public class SupervisorGradesController extends BaseController {

    private final Connection connection;
    private int id;

    private TreeItem<SupervisorGradesModel> root;

    @FXML
    private Button edit, update;

    @FXML
    private TextField fullName, examGrade, tpGrade, ccGrade;

    @FXML
    private Tab courses, semester;

    @FXML
    private TabPane gradesTabPane;

    @FXML
    private TreeTableView<SupervisorGradesModel> tableView;

    @FXML
    private TreeTableColumn<SupervisorGradesModel, String> columnFirstName, columnLastName, columnExam, columnTp, columnCc, columnTotal, columnSituation;


    public SupervisorGradesController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
    }

    @FXML
    void initialize() {
        gradesTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (oldTab != null) {
                oldTab.setContent(null);
            }
            if (newTab != null) {
                try {
                    switch (gradesTabPane.getSelectionModel().getSelectedIndex()) {
                        case 0 -> {
                            getSubjects();
                            getGrades();
                            columnExam.setText("Exam Grade");
                            columnTp.setText("TP Grade");
                            columnCc.setText("CC Grade");
                            examGrade.setDisable(false);
                            tpGrade.setDisable(false);
                            ccGrade.setDisable(false);
                            edit.setDisable(false);
                            update.setDisable(false);
                        }
                        case 1 -> {
                            getSemesters();
                            getModules();
                            columnExam.setText("Module 1");
                            columnTp.setText("Module 2");
                            columnCc.setText("Module 3");
                            examGrade.setDisable(true);
                            tpGrade.setDisable(true);
                            ccGrade.setDisable(true);
                            edit.setDisable(true);
                            update.setDisable(true);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                newTab.setContent(tableView);
            }
        });
        columnFirstName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getFirstname()));
        columnLastName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getLastname()));
        columnExam.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getExamGrade()));
        columnTp.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTpGrade()));
        columnCc.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCcGrade()));
        columnTotal.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTotalGrade()));
        columnSituation.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSituation()));

        root = new TreeItem<>(new SupervisorGradesModel("root"));
        try {
            getSubjects();
            getGrades();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setRoot(root);
        tableView.setShowRoot(false);
    }

    private void getSubjects() throws SQLException {
        root.getChildren().clear();
        String queryText = "SELECT subject FROM subject";
        ResultSet response = getResponse(connection, queryText);
        while (response.next()) {
            root.getChildren().add(new TreeItem<>(new SupervisorGradesModel(response.getString("subject"))));
        }
    }

    private void getSemesters() throws SQLException {
        root.getChildren().clear();
        for (int i=1; i <= 4; i++) {
            root.getChildren().add(new TreeItem<>(new SupervisorGradesModel("Semester "+i)));
        }
    }

    private void getGrades() throws SQLException {
        String queryText = "SELECT grades.id, student.firstname, student.lastname, exam, tp, cc, examCoeff, tpCoeff, ccCoeff, (exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01) as total, subject.id " +
                "FROM grades INNER JOIN student ON student.id = grades.student_id INNER JOIN courses ON courses.id = grades.courses_id " +
                "INNER JOIN subject ON subject.id = courses.subject_id INNER JOIN professor ON professor.subject_id = subject.id";
        ResultSet results = getResponse(connection, queryText);
        while (results.next()){
            SupervisorGradesModel supervisorGradesModel = new SupervisorGradesModel(
                    results.getInt("id"), results.getString("firstname"), results.getString("lastname"),
                    results.getInt("exam"), results.getInt("tp"),results.getInt("cc"),
                    results.getInt("total"));
            root.getChildren().get(results.getInt("subject.id")-1).getChildren().add(new TreeItem<>(supervisorGradesModel));
        }
    }

    private void getModules() throws SQLException {
        String queryText = "SELECT firstname, lastname, " +
                "module1, module2, module3, round((module1+module2+module3)/3, 2) as total " +
                "FROM " +
                "(SELECT " +
                "student.firstname, " +
                "student.lastname, " +
                "student_id, " +
                "SUM((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01)) / COUNT(DISTINCT subject) AS module1 " +
                "FROM " +
                "grades " +
                "INNER JOIN " +
                "student ON student.id = grades.student_id " +
                "INNER JOIN " +
                "courses ON courses.id = grades.courses_id " +
                "INNER JOIN " +
                "subject ON subject.id = courses.subject_id " +
                "INNER JOIN " +
                "professor ON professor.subject_id = subject.id " +
                "WHERE " +
                "semester = %1$d AND module = 1 " +
                "GROUP BY student_id ) t1 " +
                "left join (SELECT " +
                "student_id, " +
                "SUM((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01)) / COUNT(DISTINCT subject) AS module2 " +
                "FROM " +
                "grades " +
                "INNER JOIN " +
                "student ON student.id = grades.student_id " +
                "INNER JOIN " +
                "courses ON courses.id = grades.courses_id " +
                "INNER JOIN " +
                "subject ON subject.id = courses.subject_id " +
                "INNER JOIN " +
                "professor ON professor.subject_id = subject.id " +
                "WHERE " +
                "semester = %1$d AND module = 2 " +
                "GROUP BY student_id) t2 on t1.student_id = t2.student_id " +
                "left join (SELECT " +
                "student_id, " +
                "SUM((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01)) / COUNT(DISTINCT subject) AS module3 " +
                "FROM " +
                "grades " +
                "INNER JOIN " +
                "student ON student.id = grades.student_id " +
                "INNER JOIN " +
                "courses ON courses.id = grades.courses_id " +
                "INNER JOIN " +
                "subject ON subject.id = courses.subject_id " +
                "INNER JOIN " +
                "professor ON professor.subject_id = subject.id " +
                "WHERE " +
                "semester = %1$d AND module = 3 " +
                "GROUP BY student_id) t3 on t3.student_id = t1.student_id";
        for (int i=1; i<=4; i++) {
            ResultSet results = getResponse(connection, String.format(queryText, i));
            while (results.next()) {
                SupervisorGradesModel supervisorGradesModel = new SupervisorGradesModel(
                        -1, results.getString("firstname"), results.getString("lastname"),
                        results.getDouble("module1"), results.getDouble("module2"),
                        results.getDouble("module3"), results.getDouble("total"));
                root.getChildren().get(i-1).getChildren().add(new TreeItem<>(supervisorGradesModel));
            }
        }
        columnExam.setText("Module 1");
        columnTp.setText("Module 2");
        columnCc.setText("Module 3");
    }

    @FXML
    void editGrade() {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Student", "Select a column to edit");
            return;
        }
        SupervisorGradesModel student = tableView.getSelectionModel().getModelItem(index).getValue();
        if (student.getId() == -1) {
            return;
        }
        id = student.getId();
        fullName.setText(student.getFirstname() + " " + student.getLastname());
        examGrade.setText(student.getExamGrade()+"");
        tpGrade.setText(student.getTpGrade()+"");
        ccGrade.setText(student.getCcGrade()+"");

        update.setDisable(false);
        edit.setDisable(true);
    }

    @FXML
    void updateGrade() {
        String queryText = String.format(
                "UPDATE grades SET exam = '%s', tp = '%s', cc = %s WHERE (id = %d)",
                examGrade.getText(), tpGrade.getText(), ccGrade.getText(), id
        );
        try {
            executeQuery(connection, queryText);
            getSubjects();
            getGrades();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fullName.clear();
        examGrade.clear();
        tpGrade.clear();
        ccGrade.clear();
        update.setDisable(true);
        edit.setDisable(false);
    }

}

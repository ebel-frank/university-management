package com.horizons.schoolmanagement.controller;

import com.horizons.schoolmanagement.ViewFactory;
import com.horizons.schoolmanagement.database.AppDatabase;
import com.horizons.schoolmanagement.model.SupervisorGradesModel;
import com.horizons.schoolmanagement.model.SupervisorModuleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.horizons.schoolmanagement.Utils.*;

public class SupervisorGradesController extends BaseController {

    private final Connection connection;

    @FXML
    private ChoiceBox<String> year, specialty, semester, courses;

    @FXML
    private TableView<SupervisorGradesModel> coursesTableView;

    @FXML
    private TableView<SupervisorModuleModel> moduleTableView;

    @FXML
    private TableColumn<?, Void> serialNo, serialNo1;

    @FXML
    private TableColumn<SupervisorGradesModel, String> columnFirstName, columnLastName, columnTotal, columnSituation;

    @FXML
    private TableColumn<SupervisorModuleModel, String> columnFirstName1, columnLastName1, columnModule1, module1Situation, columnModule2, module2Situation, columnModule3, module3Situation, columnSemester, semesterSituation;

    @FXML
    private TableColumn<SupervisorGradesModel, Integer> columnExam, columnTp, columnCc;

    @FXML
    private Label resultTitle, back, module1Text, module2Text, module3Text;

    @FXML
    private Pane resultOptions;

    public SupervisorGradesController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
    }

    @FXML
    void initialize() {
        year.getItems().addAll("1", "2");
        year.setOnAction(event -> {
            semester.getItems().clear();
            specialty.getItems().clear();
            if (year.getValue().equals("1")) {
                semester.getItems().addAll("Semester 1", "Semester 2");
                specialty.getItems().add("TC");
                semester.setValue("Semester 1");
                specialty.setValue("TC");
                return;
            }
            semester.getItems().addAll("Semester 3", "Semester 4");
            specialty.getItems().addAll("SIC", "GE", "GME");
            semester.setValue("Semester 3");
            specialty.setValue("SIC");
        });

        semester.setOnAction(event -> {
            if (specialty.getValue() != null && semester.getValue() != null) {
                getCourses(Integer.parseInt(semester.getValue().charAt(9)+""), specialty.getValue());
            }
        });

        specialty.setOnAction(event -> {
            if (specialty.getValue() != null && semester.getValue() != null) {
                getCourses(Integer.parseInt(semester.getValue().charAt(9) + ""), specialty.getValue());
            }
        });
        year.setValue("1");

        // Configure Table View for Courses
        serialNo.setCellFactory(indexCellFactory());
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnExam.setCellValueFactory(new PropertyValueFactory<>("exam"));
        columnExam.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnExam.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE grades SET exam = %d WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getId()
            );
            event.getRowValue().setExam(event.getNewValue());
            event.getRowValue().setTotal();
            updateItem(coursesTableView.getItems(), event.getRowValue());
            try {
                executeQuery(connection, queryText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        columnTp.setCellValueFactory(new PropertyValueFactory<>("tp"));
        columnTp.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnTp.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE grades SET tp = %d WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getId()
            );
            event.getRowValue().setTp(event.getNewValue());
            event.getRowValue().setTotal();
            updateItem(coursesTableView.getItems(), event.getRowValue());
            try {
                executeQuery(connection, queryText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        columnCc.setCellValueFactory(new PropertyValueFactory<>("cc"));
        columnCc.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnCc.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE grades SET cc = %d WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getId()
            );
            event.getRowValue().setCc(event.getNewValue());
            event.getRowValue().setTotal();
            updateItem(coursesTableView.getItems(), event.getRowValue());
            try {
                executeQuery(connection, queryText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        columnSituation.setCellValueFactory(new PropertyValueFactory<>("situation"));

        // Configure Table View for Modules
        serialNo1.setCellFactory(indexCellFactory());
        columnFirstName1.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastName1.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnModule1.setCellValueFactory(new PropertyValueFactory<>("module1"));
        module1Situation.setCellValueFactory(new PropertyValueFactory<>("module1Situation"));
        columnModule2.setCellValueFactory(new PropertyValueFactory<>("module2"));
        module2Situation.setCellValueFactory(new PropertyValueFactory<>("module2Situation"));
        columnModule3.setCellValueFactory(new PropertyValueFactory<>("module3"));
        module3Situation.setCellValueFactory(new PropertyValueFactory<>("module3Situation"));
        columnSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        semesterSituation.setCellValueFactory(new PropertyValueFactory<>("semesterSituation"));

        back.setOnMouseClicked(event -> {
            resultOptions.setVisible(true);
            resultTitle.setVisible(false);
            coursesTableView.setVisible(false);
            moduleTableView.setVisible(false);
            module1Text.setVisible(false);
            module2Text.setVisible(false);
            module3Text.setVisible(false);
            back.setVisible(false);
            getCourses(Integer.parseInt(semester.getValue().charAt(9)+""), specialty.getValue());
        });

    }

    /**
     * This method updates an item in an observable list
     * @param list ObservableList item
     * @param item Object in the ObservableList
     */
    private void updateItem(ObservableList<SupervisorGradesModel> list, SupervisorGradesModel item) {
        int index = list.indexOf(item);
        if (index >= 0) {
            list.set(index, item);
        }
    }

    private void getCourses(int semester, String specialty) {
        courses.getItems().clear();
        courses.getItems().add("All Modules");
        String queryText = String.format("SELECT subject.subject FROM courses " +
                "INNER JOIN subject ON subject.id = courses.subject_id " +
                "WHERE semester = %d AND specialty = '%s'", semester, specialty);
        try {
            ResultSet response = getResponse(connection, queryText);
            while (response.next()) {
                courses.getItems().add(response.getString("subject"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (courses.getItems().size() >= 1) {
            courses.setValue(courses.getItems().get(0));
        }
    }

    private ObservableList<SupervisorGradesModel> getCourseGrades(int semester, String specialty, String course) throws SQLException {
        String queryText = String.format("SELECT grades.id, student.firstname, student.lastname, exam, tp, cc, examCoeff, tpCoeff, ccCoeff, " +
                "(exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01) AS total FROM grades INNER JOIN student ON student.id = grades.student_id " +
                "INNER JOIN courses ON courses.id = grades.courses_id INNER JOIN subject ON subject.id = courses.subject_id INNER JOIN professor ON professor.subject_id = subject.id " +
                "WHERE subject = '%s' AND semester = %d AND student.specialty = '%s'", course, semester, specialty);
        ObservableList<SupervisorGradesModel> coursesGrades = FXCollections.observableArrayList();
        ResultSet results = getResponse(connection, queryText);
        while (results.next()) {
            coursesGrades.add(new SupervisorGradesModel(
                    results.getInt("id"), results.getString("firstname"), results.getString("lastname"),
                    results.getInt("exam"), results.getInt("tp"), results.getInt("cc"),
                    results.getInt("examCoeff"), results.getInt("tpCoeff"), results.getInt("ccCoeff"), results.getInt("total")));
        }
        return coursesGrades;
    }

    private void getModuleCourses(String specialty, int semester) {
        module1Text.setText("Module 1: ");
        module2Text.setText("Module 2: ");
        module3Text.setText("Module 3: ");
        String queryText = String.format("SELECT subject.subject, module FROM courses " +
                "INNER JOIN subject ON subject.id = courses.subject_id " +
                "WHERE specialty = '%s' AND semester = %d", specialty, semester);
        try {
            ResultSet response = getResponse(connection, queryText);
            while (response.next()) {
                switch (response.getInt("module")) {
                    case 1 -> {
                        module1Text.setText(module1Text.getText()+response.getString("subject")+", ");
                    }
                    case 2 -> {
                        module2Text.setText(module2Text.getText()+response.getString("subject")+", ");
                    }
                    default -> {
                        module3Text.setText(module3Text.getText()+response.getString("subject")+", ");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<SupervisorModuleModel> getModuleGrades(String specialty, int semester) throws SQLException {
        String queryText = "SELECT firstname, lastname, " +
                "module1, module2, module3, round((module1+module2+module3)/3, 2) as total " +
                "FROM " +
                "(SELECT " +
                "student.firstname, " +
                "student.lastname, " +
                "student_id, " +
                "round(SUM((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01)) / COUNT(DISTINCT subject), 2) AS module1 " +
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
                "semester = "+semester+" AND student.specialty = '%1$s' AND module = 1 " +
                "GROUP BY student_id ) t1 " +
                "left join (SELECT " +
                "student_id, " +
                "round(SUM((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01)) / COUNT(DISTINCT subject), 2) AS module2 " +
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
                "semester = "+semester+" AND student.specialty = '%1$s' AND module = 2 " +
                "GROUP BY student_id) t2 on t1.student_id = t2.student_id " +
                "left join (SELECT " +
                "student_id, " +
                "round(SUM((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01)) / COUNT(DISTINCT subject), 2) AS module3 " +
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
                "semester = "+semester+" AND student.specialty = '%1$s' AND module = 3 " +
                "GROUP BY student_id) t3 on t3.student_id = t1.student_id";
        ObservableList<SupervisorModuleModel> moduleGrades = FXCollections.observableArrayList();
        ResultSet results = getResponse(connection, String.format(queryText, specialty));
        while (results.next()) {
            moduleGrades.add(new SupervisorModuleModel(
                    results.getString("firstname"), results.getString("lastname"), results.getFloat("module1"),
                    results.getFloat("module2"), results.getFloat("module1"), results.getFloat("total")));
        }
        return moduleGrades;
    }

    @FXML
    void submit() {
        if (courses.getValue().contains("All Modules")) {
            moduleTableView.setVisible(true);
            module1Text.setVisible(true);
            module2Text.setVisible(true);
            module3Text.setVisible(true);
            // set values in table
            try {
                getModuleCourses(specialty.getValue(), Integer.parseInt(semester.getValue().charAt(9)+""));
                moduleTableView.setItems(getModuleGrades(specialty.getValue(), Integer.parseInt(semester.getValue().charAt(9)+"")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            coursesTableView.setVisible(true);
            // set values in courses
            try {
                coursesTableView.setItems(getCourseGrades(Integer.parseInt(semester.getValue().charAt(9)+""), specialty.getValue(), courses.getValue()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        resultOptions.setVisible(false);
        resultTitle.setVisible(true);

        back.setVisible(true);
    }

}

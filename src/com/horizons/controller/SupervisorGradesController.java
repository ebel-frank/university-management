package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.SupervisorGradesModel;
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

import static com.horizons.Utils.*;

public class SupervisorGradesController extends BaseController {

    private final Connection connection;

    @FXML
    private ChoiceBox<String> year, specialty, semester, courses, course;

    @FXML
    private TableView<SupervisorGradesModel> coursesTableView;

    @FXML
    private TableColumn<?, Void> serialNo;

    @FXML
    private TableColumn<SupervisorGradesModel, String> columnFirstName, columnLastName, columnTotal, columnSituation;

    @FXML
    private TableColumn<SupervisorGradesModel, Integer> columnExam, columnTp, columnCc;

    @FXML
    private Line divider;

    @FXML
    private Label resultTitle, back;

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

        course.setOnAction(event -> {
            try {
                coursesTableView.setItems(getCourseGrades(Integer.parseInt(semester.getValue().charAt(9)+""), specialty.getValue(), course.getValue()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        back.setOnMouseClicked(event -> {
            resultOptions.setVisible(true);
            resultTitle.setVisible(false);
            coursesTableView.setVisible(false);
            course.setVisible(false);
            back.setVisible(false);
            divider.setVisible(false);
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
        courses.getItems().add("All Courses");
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

    @FXML
    void submit() {
        if (courses.getValue().contains("All Courses")) {
            courses.getItems().remove("All Courses");
            courses.setValue(courses.getItems().get(0));
            course.getItems().setAll(courses.getItems());
            course.setValue(courses.getItems().get(0));
            course.setVisible(true);
            columnExam.setVisible(false);
            columnTp.setVisible(false);
            columnCc.setVisible(false);
            columnSituation.setVisible(false);
            divider.setVisible(true);
        } else {
            columnExam.setVisible(true);
            columnTp.setVisible(true);
            columnCc.setVisible(true);
            columnSituation.setVisible(true);
        }
        resultOptions.setVisible(false);
        resultTitle.setVisible(true);
        coursesTableView.setVisible(true);
        back.setVisible(true);
        try {
            coursesTableView.setItems(getCourseGrades(Integer.parseInt(semester.getValue().charAt(9)+""), specialty.getValue(), courses.getValue()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

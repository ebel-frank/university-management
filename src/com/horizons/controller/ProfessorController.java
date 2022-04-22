package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.StudentModel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.horizons.Utils.*;

public class ProfessorController extends BaseController {

    private final int type;
    private final Connection connection;
    private ObservableList<StudentModel> students;
    private final int professorId;
    private int subjectId;
    private int id;

    @FXML
    private TabPane studentTabPane;

    @FXML
    private Tab tc, sic, gme, ge;

    @FXML
    private TableColumn<StudentModel, String> columnName, columnSpecialty, columnExam, columnTp, columnCc, columnTotal;

    @FXML
    private TableColumn<?, Void> serialNo;

    @FXML
    private TableView<StudentModel> allYearsTable;

    @FXML
    private TextField fullName, examGrade, tpGrade, ccGrade, examCoeff, tpCoeff, ccCoeff;

    @FXML
    private Label course, profileTitle, title, optionTitle, nameTitle, examTitle, tpTitle, ccTitle, examCoeffTitle, tpCoeffTitle, ccCoeffTitle;


    @FXML
    private FontAwesomeIconView profileIcon;

    @FXML
    private Button edit, update, editCoeff, updateCoeff;

    @FXML
    private MenuButton profileMenu;

    public ProfessorController(ViewFactory viewFactory, String fxmlName, int type,
            int id) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
        this.professorId = id;
    }

    @FXML
    public void initialize() {
        studentTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (oldTab != null) {
                oldTab.setContent(null);
            }
            if (newTab != null) {
                try {
                    if (newTab.equals(tc) | newTab.equals(sic) | newTab.equals(gme) | newTab.equals(ge)) {
                        columnSpecialty.setVisible(false);
                        if (newTab.equals(tc)) {
                            students = getStudents(1);
                        } else if (newTab.equals(sic)) {
                            students = getStudents(2);
                        } else if (newTab.equals(gme)) {
                            students = getStudents(3);
                        } else {
                            students = getStudents(4);
                        }
                    } else {
                        columnSpecialty.setVisible(true);
                        students = getStudents();
                    }
                    allYearsTable.setItems(students);
                    newTab.setContent(allYearsTable);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        serialNo.setCellFactory(indexCellFactory());
        columnName.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        columnSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        if (type==1) {      // else type=1 update as the Grade screen
            columnExam.setCellValueFactory(new PropertyValueFactory<>("examGrade"));
            columnTp.setCellValueFactory(new PropertyValueFactory<>("tpGrade"));
            columnCc.setCellValueFactory(new PropertyValueFactory<>("ccGrade"));
            columnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        } else {
            optionTitle.setVisible(false);
            nameTitle.setVisible(false);
            examTitle.setVisible(false);
            tpTitle.setVisible(false);
            ccTitle.setVisible(false);
            examCoeffTitle.setVisible(false);
            tpCoeffTitle.setVisible(false);
            ccCoeffTitle.setVisible(false);

            columnExam.setVisible(false);
            columnTp.setVisible(false);
            columnCc.setVisible(false);
            columnTotal.setVisible(false);

            fullName.setVisible(false);
            examGrade.setVisible(false);
            tpGrade.setVisible(false);
            ccGrade.setVisible(false);
            edit.setVisible(false);
            update.setVisible(false);
            examCoeff.setVisible(false);
            tpCoeff.setVisible(false);
            ccCoeff.setVisible(false);
            editCoeff.setVisible(false);
            updateCoeff.setVisible(false);
            AnchorPane.setRightAnchor(studentTabPane, 10.0);
        }
        try {
            ResultSet response = getProfessorDetails();
            response.next();
            title.setText(String.format("Professor %s %s", response.getString("firstname"),  response.getString("lastname")));
            profileTitle.setText(String.format("Prof. %s", response.getString("firstname")));
            course.setText(response.getString("subject"));

            examCoeff.setText(response.getString("examCoeff"));
            tpCoeff.setText(response.getString("tpCoeff"));
            ccCoeff.setText(response.getString("ccCoeff"));

            subjectId = response.getInt("subject_id");
            students = getStudents();
            allYearsTable.setItems(students);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        update.setDisable(true);
        updateCoeff.setDisable(true);
    }

    private ObservableList<StudentModel> getStudents() throws SQLException {
        return getStudents(0);
    }

    private ObservableList<StudentModel> getStudents(int specialty) throws SQLException {
        String queryText = "SELECT grades.id, firstname, lastname, student.specialty, exam, tp, cc FROM courses " +
                "INNER JOIN grades ON grades.courses_id = courses.id INNER JOIN student ON student.id = grades.student_id " +
                "WHERE courses.subject_id = "+subjectId;
        switch (specialty) {
            case 1 -> queryText = queryText + " AND student.specialty = 'TC'";
            case 2 -> queryText = queryText + " AND student.specialty = 'SIC'";
            case 3 -> queryText = queryText + " AND student.specialty = 'GME'";
            case 4 -> queryText = queryText + " AND student.specialty = 'GE'";
        }
        ResultSet response = getResponse(connection, queryText);
        ObservableList<StudentModel> students = FXCollections.observableArrayList();
        while (response.next()) {
            students.add(new StudentModel(
                    response.getInt("id"), String.format("%s %s", response.getString("firstname"), response.getString("lastname")),
                    response.getString("specialty"), response.getInt("exam"), response.getInt("tp"), response.getInt("cc"),
                    Integer.parseInt(examCoeff.getText()), Integer.parseInt(tpCoeff.getText()), Integer.parseInt(ccCoeff.getText())
            ));
        }
        return students;
    }

    private ResultSet getProfessorDetails() throws SQLException {
        String queryText = "SELECT * FROM professor INNER JOIN subject ON professor.subject_id = subject.id WHERE credentials_id = "+professorId;
        return getResponse(connection, queryText);
    }

    @FXML
    void editGrade() {
        int index = allYearsTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Professor", "Select a column to edit");
            return;
        }
        StudentModel student = students.get(index);
        id = student.getId();
        fullName.setText(student.getFullname());
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
            students.clear();
            students.addAll(getStudents(studentTabPane.getSelectionModel().getSelectedIndex()));
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

    @FXML
    void editCoeff() {
        examCoeff.setDisable(false);
        tpCoeff.setDisable(false);
        ccCoeff.setDisable(false);
        updateCoeff.setDisable(false);
        editCoeff.setDisable(true);
    }

    @FXML
    void updateCoeff() {
        if ((Integer.parseInt(examCoeff.getText()) +
                Integer.parseInt(tpCoeff.getText()) +
                Integer.parseInt(ccCoeff.getText())) != 100) {
            alert(Alert.AlertType.ERROR, "Professor", "The total of the coefficient must be equal to 100");
            return;
        }
        String queryText = String.format(
                "UPDATE professor SET examCoeff = '%s', tpCoeff = '%s', ccCoeff = '%s' WHERE (credentials_id = %d)",
        examCoeff.getText(), tpCoeff.getText(), ccCoeff.getText(), professorId);
        try {
            executeQuery(connection, queryText);
            students.clear();
            students.addAll(getStudents(studentTabPane.getSelectionModel().getSelectedIndex()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        examCoeff.setDisable(true);
        tpCoeff.setDisable(true);
        ccCoeff.setDisable(true);
        updateCoeff.setDisable(true);
        editCoeff.setDisable(false);
    }

    @FXML
    protected void showLogout() {
        profileMenu.show();
    }

    @FXML
    void logout() {
        Stage stage = (Stage) profileMenu.getScene().getWindow();
        viewFactory.closeStage(stage);
        viewFactory.showLoginWindow();
    }
}

package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.StudentModel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static com.horizons.Utils.*;

public class ProfessorController extends BaseController {

    private final int type;
    private final Connection connection;
    private final int professorId;
    private int subjectId;

    private TreeItem<StudentModel> root;

    @FXML
    private TabPane studentTabPane;

    @FXML
    private Tab all, tc, sic, gme, ge;

    @FXML
    private TreeTableColumn<StudentModel, String> columnName, columnSpecialty, columnTotal;

    @FXML
    private TreeTableColumn<StudentModel, Number> columnExam, columnTp, columnCc;

    @FXML
    private TreeTableView<StudentModel> allYearsTable;

    @FXML
    private TextField examCoeff, tpCoeff, ccCoeff;

    @FXML
    private Label course, profileTitle, title, examCoeffTitle, tpCoeffTitle, ccCoeffTitle;

    @FXML
    private TreeTableColumn<?, Void> serialNo;

    @FXML
    private FontAwesomeIconView profileIcon;

    @FXML
    private Button editCoeff, updateCoeff;

    @FXML
    private MenuButton profileMenu;

    public ProfessorController(ViewFactory viewFactory, String fxmlName, int type,
            int id) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
        this.professorId = id;
    }

    /**
     * This method is used to configure the variables
     */
    @FXML
    public void initialize() {
    	// set up the allYearsTable and it's columns
        preventColumnReordering(allYearsTable);
        allYearsTable.setRowFactory(getTreeTableViewRowFactory(allYearsTable));
        studentTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (oldTab != null) {
                oldTab.setContent(null);
            }
            if (newTab != null) {
                try {
                    if (newTab.equals(tc) | newTab.equals(sic) | newTab.equals(gme) | newTab.equals(ge)) {
                        columnSpecialty.setVisible(false);
                        if (newTab.equals(tc)) {
                            getSemesters(1);
                            getStudents(1);
                        } else if (newTab.equals(sic)) {
                            getSemesters(2);
                            getStudents(2);
                        } else if (newTab.equals(gme)) {
                            getSemesters(3);
                            getStudents(3);
                        } else {
                            getSemesters(4);
                            getStudents(4);
                        }
                    } else {
                        columnSpecialty.setVisible(true);
                        getSemesters(0);
                        getStudents(0);
                    }
                    newTab.setContent(allYearsTable);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        columnName.setCellValueFactory(param -> param.getValue().getValue().fullnameProperty());
        columnSpecialty.setCellValueFactory(param -> param.getValue().getValue().specialtyProperty());
        if (type==1) {      // when type=1 update as the Grade screen
            columnExam.setCellValueFactory(param -> param.getValue().getValue().examGradeProperty());
            // Create a custom cell factory that allows only a child tree item to be edited and not it's parent
            // this means that the drop down which has no value will not be editable but it's children will be editable.
            columnExam.setCellFactory(c -> new TextFieldTreeTableCell<>(new NumberStringConverter()) {
                @Override
                public void startEdit() {
                    if (getTreeTableRow() != null && !getTreeTableRow().isEditable()) return;
                    super.startEdit();
                }
            });
            columnExam.setOnEditCommit(event -> {
            	// update the value of the examGrade in the database when a change is made
                String queryText = String.format(
                        "UPDATE grades SET exam = %d WHERE (id = %d)",
                        event.getNewValue().intValue(), event.getRowValue().getValue().getId()
                );
                event.getRowValue().getValue().setExamGrade(event.getNewValue().intValue());
                event.getRowValue().getValue().setTotal(Integer.parseInt(examCoeff.getText()), Integer.parseInt(tpCoeff.getText()), Integer.parseInt(ccCoeff.getText()));
                try {
                    executeQuery(connection, queryText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            columnTp.setCellValueFactory(param -> param.getValue().getValue().tpGradeProperty());
            // Create a custom cell factory that allows only a child tree item to be edited and not it's parent
            columnTp.setCellFactory(c -> new TextFieldTreeTableCell<>(new NumberStringConverter()) {
                @Override
                public void startEdit() {
                    if (getTreeTableRow() != null && !getTreeTableRow().isEditable()) return;
                    super.startEdit();
                }
            });
            columnTp.setOnEditCommit(event -> {
            	// update the value of the tPGrade in the database when a change is made
                String queryText = String.format(
                        "UPDATE grades SET tp = %d WHERE (id = %d)",
                        event.getNewValue().intValue(), event.getRowValue().getValue().getId()
                );
                event.getRowValue().getValue().setTpGrade(event.getNewValue().intValue());
                event.getRowValue().getValue().setTotal(Integer.parseInt(examCoeff.getText()), Integer.parseInt(tpCoeff.getText()), Integer.parseInt(ccCoeff.getText()));

                try {
                    executeQuery(connection, queryText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            columnCc.setCellValueFactory(param -> param.getValue().getValue().ccGradeProperty());
            // Create a custom cell factory that allows only a child tree item to be edited and not it's parent
            columnCc.setCellFactory(c -> new TextFieldTreeTableCell<>(new NumberStringConverter()) {
                @Override
                public void startEdit() {
                    if (getTreeTableRow() != null && !getTreeTableRow().isEditable()) return;
                    super.startEdit();
                }
            });
            columnCc.setOnEditCommit(event -> {
            	// update the value of the cCGrade in the database when a change is made
                String queryText = String.format(
                        "UPDATE grades SET cc = %d WHERE (id = %d)",
                        event.getNewValue().intValue(), event.getRowValue().getValue().getId()
                );
                event.getRowValue().getValue().setCcGrade(event.getNewValue().intValue());
                event.getRowValue().getValue().setTotal(Integer.parseInt(examCoeff.getText()), Integer.parseInt(tpCoeff.getText()), Integer.parseInt(ccCoeff.getText()));
                try {
                    executeQuery(connection, queryText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            columnTotal.setCellValueFactory(param -> param.getValue().getValue().totalProperty());
        } else { // update as the students screen
            serialNo.setVisible(true);
            serialNo.setCellFactory(treeIndexCellFactory());

            examCoeffTitle.setVisible(false);
            tpCoeffTitle.setVisible(false);
            ccCoeffTitle.setVisible(false);

            columnExam.setVisible(false);
            columnTp.setVisible(false);
            columnCc.setVisible(false);
            columnTotal.setVisible(false);

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

            root = new TreeItem<>(new StudentModel("root"));
            setTabs();

            allYearsTable.setRoot(root);
            allYearsTable.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateCoeff.setDisable(true);
    }

    /**
     * This ensures that the specialty tabs available for a professor are only from the specialty he teaches
     */
    private void setTabs() {
        String queryText = "SELECT specialty FROM professor INNER JOIN courses ON courses.subject_id = professor.subject_id " +
                "WHERE professor.subject_id = "+subjectId;
        try {
            ResultSet response = getResponse(connection, queryText);
            Set<String> specialties = new HashSet<>();
            while (response.next()) {
                specialties.add(response.getString("specialty"));
            }
            if(specialties.size() == 1) {
                all.setStyle("-fx-pref-width: 0;");
                columnSpecialty.setVisible(false);
                if (specialties.contains("TC")) {
                    studentTabPane.getSelectionModel().select(tc);
                } else if (specialties.contains("SIC")) {
                    studentTabPane.getSelectionModel().select(sic);
                } else if (specialties.contains("GME")) {
                    studentTabPane.getSelectionModel().select(gme);
                } else {
                    studentTabPane.getSelectionModel().select(ge);
                }
            } else {
                getSemesters(0);
                getStudents(0);
            }
            if (!specialties.contains("TC")) {
                tc.setStyle("-fx-pref-width: 0;");
            }
            if (!specialties.contains("SIC")) {
                sic.setStyle("-fx-pref-width: 0;");
            }
            if (!specialties.contains("GME")) {
                gme.setStyle("-fx-pref-width: 0;");
            }
            if (!specialties.contains("GE")) {
                ge.setStyle("-fx-pref-width: 0;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This adds the students to their respective semester tree item
     * @param specialty 
     * @throws SQLException
     */
    private void getStudents(int specialty) throws SQLException {
        String queryText = "SELECT grades.id, firstname, lastname, student.specialty, exam, tp, cc, semester FROM courses " +
                "INNER JOIN grades ON grades.courses_id = courses.id INNER JOIN student ON student.id = grades.student_id " +
                "WHERE courses.subject_id = "+subjectId;
        int diff = 1;
        if (specialty >= 2) {
            diff = 3;
        }
        switch (specialty) {
            case 1 -> queryText += " AND student.specialty = 'TC'";
            case 2 -> queryText += " AND student.specialty = 'SIC'";
            case 3 -> queryText += " AND student.specialty = 'GME'";
            case 4 -> queryText += " AND student.specialty = 'GE'";
        }

        if (type == 0) {
            ResultSet response = getResponse(connection, queryText+" GROUP BY student_id");
            while (response.next()) {
                StudentModel student = new StudentModel(
                        String.format("%s %s", response.getString("firstname"), response.getString("lastname")),
                        response.getString("specialty"));
                root.getChildren().add(new TreeItem<>(student));
            }
            return;
        } else {
            ResultSet response = getResponse(connection, queryText+"GROUP BY student_id");
            while (response.next()) {
                int examGrade = response.getInt("exam");
                int tpGrade = response.getInt("tp");
                int ccGrade = response.getInt("cc");
                StudentModel student = new StudentModel(
                        response.getInt("id"), String.format("%s %s", response.getString("firstname"), response.getString("lastname")),
                        response.getString("specialty"), examGrade, tpGrade, ccGrade, (examGrade * Integer.parseInt(examCoeff.getText()) * 0.01) + (tpGrade * Integer.parseInt(tpCoeff.getText()) * 0.01) + (ccGrade * Integer.parseInt(ccCoeff.getText()) * 0.01)
                );
                root.getChildren().get(response.getInt("semester")-diff).getChildren().add(new TreeItem<>(student));
            }
        }

        // This removes a semester tree item if there is no student model in it
        root.getChildren().removeIf(studentModelTreeItem -> studentModelTreeItem.getChildren().size() == 0);
    }

    /**
     * This adds all the semesters as a child to the root tree item
     * @param specialty
     */
    private void getSemesters(int specialty) {
        root.getChildren().clear();
        if (type == 0) {
            return;
        }
        int lower = 1, upper = 4;
        switch (specialty) {
            case 1 -> upper = 2;
            case 2, 3, 4 -> lower = 3;
        }
        for (int i=lower; i <= upper; i++) {
            root.getChildren().add(new TreeItem<>(new StudentModel("Semester "+i)));
        }
    }

    /**
     * @return	the details of the professor using the professor id
     * @throws SQLException
     */
    private ResultSet getProfessorDetails() throws SQLException {
        String queryText = "SELECT * FROM professor INNER JOIN subject ON professor.subject_id = subject.id WHERE credentials_id = "+professorId;
        return getResponse(connection, queryText);
    }

    /**
     * This method enables the text fields to be edited by the professor
     */
    @FXML
    void editCoeff() {
        examCoeff.setDisable(false);
        tpCoeff.setDisable(false);
        ccCoeff.setDisable(false);
        updateCoeff.setDisable(false);
        editCoeff.setDisable(true);
    }

    /**
     * This method updates the coefficient of the professor using the professor id
     */
    @FXML
    void updateCoeff() {
        try {
            if ((Integer.parseInt(examCoeff.getText()) +
                    Integer.parseInt(tpCoeff.getText()) +
                    Integer.parseInt(ccCoeff.getText())) != 100) {
                alert(Alert.AlertType.ERROR, "Professor", "The total of the coefficient must be equal to 100");
                return;
            }
            String queryText = String.format(
                    "UPDATE professor SET examCoeff = '%s', tpCoeff = '%s', ccCoeff = '%s' WHERE (credentials_id = %d)",
                    examCoeff.getText(), tpCoeff.getText(), ccCoeff.getText(), professorId);

            executeQuery(connection, queryText);
            getSemesters(studentTabPane.getSelectionModel().getSelectedIndex());
            getStudents(studentTabPane.getSelectionModel().getSelectedIndex());
        } catch (SQLException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Coefficient", "Invalid data entry");

        }
        examCoeff.setDisable(true);
        tpCoeff.setDisable(true);
        ccCoeff.setDisable(true);
        updateCoeff.setDisable(true);
        editCoeff.setDisable(false);
    }

    /**
     * This shows the logout dropdown-button 
     */
    @FXML
    protected void showLogout() {
        profileMenu.show();
    }

    /**
     * Logs the user out of the application
     */
    @FXML
    void logout() {
        Stage stage = (Stage) profileMenu.getScene().getWindow();
        viewFactory.closeStage(stage);
        viewFactory.showLoginWindow();
    }

    /**
     * Takes the user back to the welcome screen
     */
    @FXML
    void goBack() {
        Stage stage = (Stage) profileMenu.getScene().getWindow();
        viewFactory.goBack(stage);
    }
}

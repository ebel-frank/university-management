package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.GradesModel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.horizons.Utils.getResponse;
import static com.horizons.Utils.preventColumnReordering;

public class StudentController extends BaseController {

    private final int type;
    private final Connection connection;
    private int studentId;
    private int studentYear;

    private TreeItem<GradesModel> root, module1, module2, module3;

    @FXML
    private MenuItem logout;

    @FXML
    private FontAwesomeIconView profileIcon;

    @FXML
    private MenuButton profileMenu;

    @FXML
    private Tab semester1, semester2;

    @FXML
    private TabPane studentTabPane;
    
    @FXML
    private Button transcriptBtn;

    @FXML
    private Label profileTitle, title, year, specialty;

    @FXML
    private TreeTableView<GradesModel> tableView;

    @FXML
    private TreeTableColumn<GradesModel, String> columnSubject, columnExam, columnTp, columnCc, columnTotal;

    /**
     * Constructor of the StudentController
     * @param viewFactory 	The ViewFactory object which will manage the layout
     * @param fxmlName		The fxml name of this controller
     * @param type			We use this to tell our application whether to set up interface of "Modules" or "Grades"
     * @param id			The student id, which we will use to get the student details like his/her name, courses and grades
     */
    public StudentController(ViewFactory viewFactory, String fxmlName, int type,
                             int id) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
        this.studentId = id;
    }

	@FXML
    public void initialize() {
		// set up the tableView and it's columns
        preventColumnReordering(tableView);
        studentTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (oldTab != null) {
                oldTab.setContent(null);
            }
            if (newTab != null) {
                try {
                    getStudentGrades(studentTabPane.getSelectionModel().getSelectedIndex());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                newTab.setContent(tableView);
            }
        });
        columnSubject.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSubject()));
        columnExam.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getExamGrade()));
        columnTp.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTpGrade()));
        columnCc.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCcGrade()));
        columnTotal.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTotalGrade()));
        if (type == 0) {	// if type = 0, then set up the interface for "Modules"
            tableView.getStyleClass().add("removeheader");
            columnExam.setVisible(false);
            columnTp.setVisible(false);
            columnCc.setVisible(false);
            columnCc.setVisible(false);
            columnTotal.setVisible(false);
            transcriptBtn.setVisible(false);
        }

        root = new TreeItem<>(new GradesModel("root"));	// This is the root of the TreeTableView
        module1 = new TreeItem<>(new GradesModel("Module 1"));
        module2 = new TreeItem<>(new GradesModel("Module 2"));
        module3 = new TreeItem<>(new GradesModel("Module 3"));

        try {
            ResultSet response = getStudentDetails();
            response.next();
            studentId = response.getInt("id");  // replace the student credential id with the student id
            title.setText(String.format("%s %s", response.getString("firstname"), response.getString("lastname")));
            profileTitle.setText(String.format("%s", response.getString("firstname")));
            studentYear = response.getInt("year");
            year.setText("Year " + studentYear);
            specialty.setText("Specialty: " + response.getString("specialty"));

            if (studentYear!=1) {
                semester1.setText("Semester 3");
                semester2.setText("Semester 4");
            }

            getStudentGrades(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //noinspection unchecked
        root.getChildren().setAll(module1, module2, module3);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
    }

	/**
	 * @return the details of a student using the student id
	 * @throws SQLException
	 */
    private ResultSet getStudentDetails() throws SQLException {
        String queryText = "SELECT id, firstname, lastname, year, specialty FROM student WHERE credentials_id = " + studentId;
        return getResponse(connection, queryText);
    }

    /**
     * This method loads the Table view with the grades of the student
     * @param semester	used to specify which grades to load in the table view based on the given semester
     * @throws SQLException
     */
    private void getStudentGrades(int semester) throws SQLException {
        module1.getChildren().clear();
        module2.getChildren().clear();
        module3.getChildren().clear();
        String queryText = "SELECT subject, module, exam,tp, cc, examCoeff, tpCoeff, ccCoeff  " +
                "FROM grades INNER JOIN courses ON courses.id = grades.courses_id INNER JOIN subject ON subject.id = courses.subject_id " +
                "INNER JOIN professor ON subject.id = professor.subject_id WHERE student_id = "+studentId+" AND semester = "+(semester+(2*studentYear)-1);
        ResultSet response = getResponse(connection, queryText);
        while (response.next()) {
            GradesModel grade = new GradesModel(response.getString("subject"), response.getInt("exam"),
                    response.getInt("tp"), response.getInt("cc"), response.getInt("examCoeff"),
                    response.getInt("tpCoeff"), response.getInt("ccCoeff"));
            switch (response.getInt("module")) {
                case 1 -> {
                    module1.getChildren().add(new TreeItem<>(grade));
                }
                case 2 -> {
                    module2.getChildren().add(new TreeItem<>(grade));
                }
                case 3 -> {
                    module3.getChildren().add(new TreeItem<>(grade));
                }
            }
        }
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
    
    /**
     * This takes the user to the transcriptWindow, where the user can print his/her transcript
     */
    @FXML
    void showTranscript() {
        BaseController controller = new TranscriptController(
                viewFactory, "transcript_application.fxml", title.getText(),
                studentYear, specialty.getText().substring(11), studentId, (Stage) profileMenu.getScene().getWindow());
        viewFactory.showTranscriptWindow(controller);
    }

}

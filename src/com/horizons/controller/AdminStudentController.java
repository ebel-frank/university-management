package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.AdminStudentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import static com.horizons.Utils.*;

public class AdminStudentController extends BaseController {

    private final Connection connection;
    private ObservableList<AdminStudentModel> students;
    private final int type;

    @FXML
    private Button add, delete;

    @FXML
    private Label optionTitle, firstNameTitle, lastNameTitle, emailTitle, passwordTitle, specialtyTitle, yearTitle;

    @FXML
    private TableView<AdminStudentModel> allYearsTable;

    @FXML
    private TableColumn<AdminStudentModel, String> columnId, columnFirstName, columnLastName, columnYear, columnEmail, columnPassword, columnSpecialty;

    @FXML
    private TextField firstName, lastName, email, password;

    @FXML
    private ChoiceBox<String> year, specialty;

    @FXML
    private TabPane studentTabPane;

    @FXML
    private Tab year1, year2;

    /**
     * Constructor of the AdminStudentController
     * @param viewFactory	The ViewFactory object which will manage the layout
     * @param fxmlName		The fxml name of this controller
     * @param type			It tells our application to set up the interface of "View Information" or "Add Information"
     */
    public AdminStudentController(ViewFactory viewFactory, String fxmlName, int type) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
    }

    /**
     * This method is used to configure the variables
     */
    @FXML
    void initialize() {
        preventColumnReordering(allYearsTable);
        if (type == 0) { 	// when type = 0 then we set up the interface for "View Information"
            optionTitle.setVisible(false);
            firstNameTitle.setVisible(false);
            lastNameTitle.setVisible(false);
            emailTitle.setVisible(false);
            passwordTitle.setVisible(false);
            specialtyTitle.setVisible(false);
            yearTitle.setVisible(false);

            firstName.setVisible(false);
            lastName.setVisible(false);
            email.setVisible(false);
            password.setVisible(false);
            year.setVisible(false);
            specialty.setVisible(false);
            add.setVisible(false);
            delete.setVisible(false);
            AnchorPane.setRightAnchor(studentTabPane, 10.0);

        } else {	// when type = 1 then we set up the interface for "Add Information"
            columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            columnPassword.setCellFactory(TextFieldTableCell.forTableColumn());
            columnSpecialty.setCellFactory(ChoiceBoxTableCell.forTableColumn("TC", "SIC","GE","GME"));

        }
        // configure the tab pane to use one table for all it's tabs
        studentTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (oldTab != null) {
                oldTab.setContent(null);
            }
            if (newTab != null) {
                try {
                    if(newTab.equals(year1) | newTab.equals(year2)) {
                        columnYear.setVisible(false);
                        if (newTab.equals(year1)) {
                            students = getStudents(1);
                            allYearsTable.setItems(students);
                            year.setValue("1");
                        } else {
                            students = getStudents(2);
                            allYearsTable.setItems(students);
                            year.setValue("2");
                        }
                        year.setDisable(true);
                    } else {
                        columnYear.setVisible(true);
                        students = getStudents();
                        allYearsTable.setItems(students);
                        year.setDisable(false);
                    }
                    newTab.setContent(allYearsTable);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnFirstName.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE student SET firstname = '%s' WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getId()
            );
            event.getRowValue().setFirstname(event.getNewValue());
            try {
                executeQuery(connection, queryText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnLastName.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE student SET lastname = '%s' WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getId()
            );
            event.getRowValue().setLastname(event.getNewValue());
            try {
                executeQuery(connection, queryText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnEmail.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE credentials SET email = '%s' WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getCredentialID()
            );
            try {
                executeQuery(connection, queryText);
                event.getRowValue().setEmail(event.getNewValue());
            } catch (SQLException e) {
                // Operation failed then we show the user an alert with the error message
                alert(Alert.AlertType.ERROR, "Student", "Email already exists");
                int index = students.indexOf(event.getRowValue());
                if (index >= 0) {
                    students.set(index, event.getRowValue());
                }
            }
        });
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        columnPassword.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE credentials SET password = '%s' WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getCredentialID()
            );
            event.getRowValue().setPassword(event.getNewValue());
            try {
                executeQuery(connection, queryText);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        columnSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        columnSpecialty.setOnEditCommit(event -> {
            int year = 2;
            String newValue = event.getNewValue();
            if (!Objects.equals(newValue, event.getRowValue().getSpecialty())) {
                if (Objects.equals(newValue, "TC")) {
                    year = 1;
                }
                String queryText = String.format(
                        "UPDATE student SET year = %d, specialty = '%s' WHERE (id = %d)",
                        year, event.getNewValue(), event.getRowValue().getId()
                );
                event.getRowValue().setSpecialty(event.getNewValue());
                event.getRowValue().setYear(year);
                new Thread(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        executeQuery(connection, queryText);

                        unregisterStudentForCourses(connection, event.getRowValue().getId());
                        registerStudentForCourses(connection, event.getRowValue().getSpecialty(), event.getRowValue().getId());
                        return null;
                    }
                }).start();
                int index = students.indexOf(event.getRowValue());
                if (index >= 0) {
                    students.set(index, event.getRowValue());
                }
            }
        });

        try {
            students = getStudents();
            allYearsTable.setItems(students);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        year.getItems().addAll("1", "2");
        year.setOnAction(event -> {
            specialty.getItems().clear();
            if(year.getValue().equals("1")) {
                specialty.getItems().add("TC");
                specialty.setValue("TC");
                return;
            }
            specialty.getItems().addAll("SIC","GE","GME");
            specialty.setValue("SIC");
        });
        year.setValue("1");
    }

    
    /**
     * This method returns the list of students in the database
     * @param year	the year of the students
     * @return		An ObservableList of students
     * @throws SQLException
     */
    private ObservableList<AdminStudentModel> getStudents(int year) throws SQLException {
        String queryText;
        if (year == 0) {	// when year = 0, then we query all the student
            queryText = "SELECT * FROM student";
        } else {
            queryText = "SELECT * FROM student WHERE year = "+year;
        }

        ResultSet response = getResponse(connection, queryText);
        ObservableList<AdminStudentModel> students = FXCollections.observableArrayList();
        ResultSet resultSet;
        while (response.next()) {
            resultSet = getResponse(connection, "SELECT email, password FROM credentials WHERE id = "+response.getInt("credentials_id"));
            resultSet.next();
            students.add(new AdminStudentModel(
                    response.getInt("id"), response.getInt("credentials_id"),  response.getString("firstname"), response.getString("lastname"),
                    resultSet.getString("email"), resultSet.getString("password"), response.getInt("year"), response.getString("specialty")
            ));
        }
        return students;
    }

    /**
     * This is what is known as method overloading, I used it because 0 is a known value
     * I don't want to type it every time I want to get all the students.
     * @return
     * @throws SQLException
     */
    private ObservableList<AdminStudentModel> getStudents() throws SQLException {
        return getStudents(0);
    }

    /**
     * This adds a student to the database. It first adds the students email and password to
     * the information table, then it adds the student details to the student table.
     */
    @FXML
    void addUser() {
        if (firstName.getText().isEmpty() | lastName.getText().isEmpty() | email.getText().isEmpty() | password.getText().isEmpty()) {
            alert(Alert.AlertType.ERROR, "Student", "All fields are required");
            return;
        }
        String queryText = String.format(
                "INSERT INTO credentials (email, password, user) VALUES ('%s', '%s', 0)",
                email.getText(), password.getText());
        try {
            executeQuery(connection, queryText);
            queryText = String.format("SELECT id FROM credentials WHERE email = '%s'", email.getText());
            ResultSet response = getResponse(connection, queryText);
            response.next();
            queryText = String.format(
                    "INSERT INTO student (credentials_id, firstname, lastname, year, specialty) VALUES (%d, '%s', '%s', '%s', '%s')",
                    response.getInt("id"), firstName.getText(), lastName.getText(), year.getValue(), specialty.getValue());
            executeQuery(connection, queryText);
            int credentialID = response.getInt("id");
            queryText = String.format("SELECT id FROM student WHERE credentials_id = %d", credentialID);
            response = getResponse(connection, queryText);
            response.next();
            students.add(new AdminStudentModel(response.getInt("id"), credentialID, firstName.getText(), lastName.getText(), email.getText(), password.getText(), Integer.parseInt(year.getValue()), specialty.getValue()));
            registerStudentForCourses(connection, specialty.getValue(), response.getInt("id"));
        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "Credentials", e.getMessage());
        }
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
    }

    /**
     * This deletes a student from the database
     */
    @FXML
    void deleteUser() {
        int index = allYearsTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Student", "Select a column to delete");
            return;
        }
        AdminStudentModel student = students.get(index);
        Optional<ButtonType> option = alert(Alert.AlertType.CONFIRMATION, "Delete", "Are you sure you want to delete this record");
        option.ifPresent(buttonType -> {
            if (ButtonType.OK.equals(buttonType)) {
                try {
                    students.remove(student);
                    unregisterStudentForCourses(connection, student.getId());
                    String queryText = String.format(
                            "DELETE FROM student WHERE (id = %d)",
                            student.getId()
                    );
                    executeQuery(connection, queryText);
                    queryText = String.format(
                            "DELETE FROM credentials WHERE (id = %d)",
                            student.getCredentialID()
                    );
                    executeQuery(connection, queryText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

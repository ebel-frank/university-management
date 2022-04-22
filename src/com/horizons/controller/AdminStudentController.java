package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.AdminStudentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.horizons.Utils.*;

public class AdminStudentController extends BaseController {

    private final Connection connection;
    private ObservableList<AdminStudentModel> students;
    private int id, credentialId;
    private final int type;

    @FXML
    private Button add, edit, update, delete;

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

    public AdminStudentController(ViewFactory viewFactory, String fxmlName, int type) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
    }

    @FXML
    void initialize() {
        if (type == 0) {
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
            edit.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
            AnchorPane.setRightAnchor(studentTabPane, 10.0);

        }
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
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        columnSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));

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

        update.setDisable(true);
    }

    private ObservableList<AdminStudentModel> getStudents(int year) throws SQLException {
        String queryText;
        if (year == 0) {
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

    private ObservableList<AdminStudentModel> getStudents() throws SQLException {
        return getStudents(0);
    }

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

    @FXML
    void editUser() {
        int index = allYearsTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Student", "Select a column to edit");
            return;
        }
        AdminStudentModel student = students.get(index);
        id = student.getId();
        credentialId = student.getCredentialID();
        firstName.setText(student.getFirstname());
        lastName.setText(student.getLastname());
        email.setText(student.getEmail());
        password.setText(student.getPassword());
        year.setValue(student.getYear()+"");
        specialty.setValue(student.getSpecialty());
        add.setDisable(true);
        update.setDisable(false);
        delete.setDisable(true);
    }

    @FXML
    void updateUser() {
        String queryText = String.format(
                "UPDATE student SET firstname = '%s', lastname = '%s', year = %s, specialty = '%s' WHERE (id = %d)",
                firstName.getText(), lastName.getText(), year.getValue(), specialty.getValue(), id
        );
        try {
            executeQuery(connection, queryText);
            queryText = String.format(
                    "UPDATE credentials SET email = '%s', password = '%s' WHERE (id = %d)",
                    email.getText(), password.getText(), credentialId
            );
            executeQuery(connection, queryText);
            students.clear();
            students.addAll(getStudents(studentTabPane.getSelectionModel().getSelectedIndex()));
            unregisterStudentForCourses(connection, id);
            registerStudentForCourses(connection, specialty.getValue(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
        update.setDisable(true);
        add.setDisable(false);
        delete.setDisable(false);
    }

}

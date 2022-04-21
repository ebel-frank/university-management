package main.java.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.ViewFactory;
import main.java.database.AppDatabase;
import main.java.model.AdminProfessorModel;

import static main.java.Utils.*;
import static main.java.Utils.alert;

public class AdminProfessorController extends BaseController {

    private final Connection connection;
    private ObservableList<AdminProfessorModel> professors;
    private int id, credentialId;
    private final int type;

    @FXML
    private Button add, edit, update, delete;

    @FXML
    private Label optionTitle, firstNameTitle, lastNameTitle, emailTitle, passwordTitle, subjectTitle;

    @FXML
    private TableView<AdminProfessorModel> professorTable;

    @FXML
    private TableColumn<AdminProfessorModel, String> columnId, columnFirstName, columnLastName, columnEmail, columnPassword, columnSubject;

    @FXML
    private TextField firstName, lastName, email, password;

    @FXML
    private ChoiceBox<String> subject;

    public AdminProfessorController(ViewFactory viewFactory, String fxmlName, int type) {
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
            subjectTitle.setVisible(false);

            firstName.setVisible(false);
            lastName.setVisible(false);
            email.setVisible(false);
            password.setVisible(false);
            subject.setVisible(false);
            add.setVisible(false);
            edit.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
            AnchorPane.setRightAnchor(professorTable, 10.0);

        }
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        columnSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));

        try {
            professors = getProfessors();
            professorTable.setItems(professors);

            String queryText = "SELECT subject FROM subject";
            ResultSet response = getResponse(connection, queryText);
            while (response.next()) {
                subject.getItems().add(response.getString("subject"));
                if (subject.getItems().size() == 1) {
                    subject.setValue(subject.getItems().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        update.setDisable(true);
    }

    private ObservableList<AdminProfessorModel> getProfessors() throws SQLException {
        String queryText = "SELECT professor.id, credentials_id, firstname, lastname, subject FROM professor INNER JOIN subject ON professor.subject_id = subject.id";
        ResultSet response = getResponse(connection, queryText);
        ObservableList<AdminProfessorModel> professors = FXCollections.observableArrayList();
        ResultSet resultSet;
        while (response.next()) {
            resultSet = getResponse(connection, "SELECT email, password FROM credentials WHERE id = "+response.getInt("credentials_id"));
            resultSet.next();
            professors.add(new AdminProfessorModel(
                    response.getInt("id"), response.getInt("credentials_id"), response.getString("firstname"),
                    response.getString("lastname"), resultSet.getString("email"), resultSet.getString("password"), response.getString("subject")
            ));
        }
        return professors;
    }

    private int getSubjectId(String course) throws SQLException {
        String queryText = String.format("SELECT id FROM subject WHERE subject = '%s'", course);
        ResultSet response = getResponse(connection, queryText);
        response.next();
        return response.getInt("id");
    }

    @FXML
    void addUser() {
        if (firstName.getText().isEmpty() | lastName.getText().isEmpty() | email.getText().isEmpty() | password.getText().isEmpty()) {
            alert(Alert.AlertType.ERROR, "Professor", "All fields are required");
            return;
        }
        Savepoint savepoint = null;
        String queryText = String.format(
                "INSERT INTO credentials (email, password, user) VALUES ('%s', '%s', 1)",
                email.getText(), password.getText());
        try {
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint();
            executeQuery(connection, queryText);
            queryText = String.format("SELECT id FROM credentials WHERE email = '%s'", email.getText());
            ResultSet response = getResponse(connection, queryText);
            response.next();
            int credentialID = response.getInt("id");
            queryText = String.format(
                    "INSERT INTO professor (credentials_id, firstname, lastname, subject_id) VALUES ('%s', '%s', '%s', %d)",
                    credentialID, firstName.getText(), lastName.getText(), getSubjectId(subject.getValue()));
            executeQuery(connection, queryText);
            queryText = String.format("SELECT id FROM professor WHERE credentials_id = '%s'", credentialID);
            response = getResponse(connection, queryText);
            response.next();
            professors.add(new AdminProfessorModel(response.getInt("id"), credentialID, firstName.getText(), lastName.getText(), email.getText(), password.getText(), subject.getValue()));
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "Credentials", e.getMessage());
            try {
                AppDatabase.getConnection().rollback(savepoint);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return;
        }
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
    }

    @FXML
    void deleteUser() {
        int index = professorTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Professor", "Select a column to delete");
            return;
        }
        AdminProfessorModel professor = professors.get(index);
        String queryText = String.format(
                "DELETE FROM professor WHERE (id = %d)",
                professor.getId()
        );
        Optional<ButtonType> option = alert(Alert.AlertType.CONFIRMATION, "Delete", "Are you sure you want to delete this record");
        option.ifPresent(buttonType -> {
            if (ButtonType.OK.equals(buttonType)) {
                try {
                    professors.remove(professor);
                    executeQuery(connection, queryText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void editUser() {
        int index = professorTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Professor", "Select a column to edit");
            return;
        }
        AdminProfessorModel professor = professors.get(index);
        id = professor.getId();
        credentialId = professor.getCredentialID();
        firstName.setText(professor.getFirstname());
        lastName.setText(professor.getLastname());
        email.setText(professor.getEmail());
        password.setText(professor.getPassword());
        subject.setValue(professor.getSubject());
        add.setDisable(true);
        update.setDisable(false);
        delete.setDisable(true);
    }

    @FXML
    void updateUser() {
        try {
            String queryText = String.format(
                    "UPDATE professor SET firstname = '%s', lastname = '%s', subject_id = %d WHERE (id = %d)",
                    firstName.getText(), lastName.getText(), getSubjectId(subject.getValue()), id
            );
            executeQuery(connection, queryText);
            queryText = String.format(
                    "UPDATE credentials SET email = '%s', password = '%s' WHERE (id = %d)",
                    email.getText(), password.getText(), credentialId
            );
            executeQuery(connection, queryText);
            professors.clear();
            professors.addAll(getProfessors());
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

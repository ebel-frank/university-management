package com.horizons.controller;

import com.horizons.database.AppDatabase;
import com.horizons.model.AdminProfessorModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.horizons.Utils.*;
import static com.horizons.Utils.alert;

public class AdminProfessorController {

    private Connection connection;
    private ObservableList<AdminProfessorModel> professors;
    private final List<String> subjects = new ArrayList<>();

    @FXML
    private Button add, delete;

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

    /**
     * This method is used to configure the variables
     * @param type	It tells our application to set up the interface of "View Information" or "Add Information"
     */
    public void setUpVariables(int type) {
    	this.connection = AppDatabase.getConnection();
    	
    	// set up the professorTable and it's columns
        preventColumnReordering(professorTable);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnFirstName.setOnEditCommit(event -> {
        	// update the value of the firstname in the database when a change is made
            String queryText = String.format(
                    "UPDATE professor SET firstname = '%s' WHERE (id = %d)",
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
        	// update the value of the lastname in the database when a change is made
            String queryText = String.format(
                    "UPDATE professor SET lastname = '%s' WHERE (id = %d)",
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
        	// update the value of the email in the database when a change is made
            String queryText = String.format(
                    "UPDATE credentials SET email = '%s' WHERE (id = %d)",
                    event.getNewValue(), event.getRowValue().getCredentialID()
            );
            try {
                executeQuery(connection, queryText);
                event.getRowValue().setEmail(event.getNewValue());
            } catch (SQLException e) {
                // Operation Failed so we update the user with an error alert
                alert(Alert.AlertType.ERROR, "Professor", "Email already exists");
                int index = professors.indexOf(event.getRowValue());
                if (index >= 0) {
                    professors.set(index, event.getRowValue());
                }
            }
        });
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        columnPassword.setOnEditCommit(event -> {
        	// update the value of the password in the database when a change is made
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
        columnSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        columnSubject.setOnEditCommit(event -> {
        	// update the value of the subject in the database when a change is made
            try {
                String queryText = String.format(
                        "UPDATE professor SET subject_id = %d WHERE (id = %d)",
                        getSubjectId(event.getNewValue()), event.getRowValue().getId()
                );
                executeQuery(connection, queryText);
                event.getRowValue().setSubject(event.getNewValue());
            } catch (SQLException e) {
                // Operation Failed, then we reload the row and inform the user about the error with an alert dialog
                alert(Alert.AlertType.ERROR, "Professor", "The subject is already taken by a different professor");
                int index = professors.indexOf(event.getRowValue());
                if (index >= 0) {
                    professors.set(index, event.getRowValue());
                }
            }
        });
        try {
        	// set the list of professors to the professor table
            professors = getProfessors();
            professorTable.setItems(professors);

            String queryText = "SELECT subject FROM subject";
            ResultSet response = getResponse(connection, queryText);
            while (response.next()) {
                subjects.add(response.getString("subject"));
            }
            subject.getItems().addAll(subjects);
            if (subject.getItems().size() == 1) {
                subject.setValue(subject.getItems().get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (type == 0) {	// when type = 0 then we set up the interface for "View Information"
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
            add.setVisible(false);;
            delete.setVisible(false);
            AnchorPane.setRightAnchor(professorTable, 10.0);

        } else {	// when type = 1 then we set up the interface for "Add Information"
            columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            columnPassword.setCellFactory(TextFieldTableCell.forTableColumn());
            columnSubject.setCellFactory(ChoiceBoxTableCell.forTableColumn(subjects.toArray(String[]::new)));
        }
    }

    /**
     * @return the list of all the professors in the database
     * @throws SQLException
     */
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

    /**
     * @param course 	the subject title
     * @return 			the id of the given subject title
     * @throws SQLException
     */
    private int getSubjectId(String course) throws SQLException {
        String queryText = String.format("SELECT id FROM subject WHERE subject = '%s'", course);
        ResultSet response = getResponse(connection, queryText);
        response.next();
        return response.getInt("id");
    }

    /**
     * This adds a professor to the database: It first adds the professor email and password to
     * the information table, then it add the professor details to the professor table.
     */
    @FXML
    void addUser() {
    	// check that all the fields is not empty
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

    /**
     * This deletes a professor from the database
     */
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

}

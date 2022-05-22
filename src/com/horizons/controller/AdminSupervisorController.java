package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.AdminSupervisorModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.horizons.Utils.*;
import static com.horizons.Utils.alert;

public class AdminSupervisorController extends BaseController {

    private final Connection connection;
    private ObservableList<AdminSupervisorModel> supervisors;
    private int id, credentialId;
    private final int type;

    @FXML
    private Button add, delete;

    @FXML
    private Label optionTitle, firstNameTitle, lastNameTitle, emailTitle, passwordTitle;

    @FXML
    private TableView<AdminSupervisorModel> supervisorTable;

    @FXML
    private TableColumn<AdminSupervisorModel, String> columnId, columnFirstName, columnLastName, columnEmail, columnPassword;

    @FXML
    private TextField firstName, lastName, email, password;

    /**
     * Constructor of the AdminSupervisorController
     * @param viewFactory	The ViewFactory object which will manage the layout
     * @param fxmlName		The fxml name of this controller
     * @param type			It tells our application to set up the interface of "View Information" or "Add Information"
     */
    public AdminSupervisorController(ViewFactory viewFactory, String fxmlName, int type) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
    }

    @FXML
    void initialize() {
        preventColumnReordering(supervisorTable);
        if (type == 0) {	// when type = 0 then we set up the interface for "View Information"
            optionTitle.setVisible(false);
            firstNameTitle.setVisible(false);
            lastNameTitle.setVisible(false);
            emailTitle.setVisible(false);
            passwordTitle.setVisible(false);

            firstName.setVisible(false);
            lastName.setVisible(false);
            email.setVisible(false);
            password.setVisible(false);
            add.setVisible(false);
            delete.setVisible(false);
            AnchorPane.setRightAnchor(supervisorTable, 10.0);
        } else {	// when type = 1 then we set up the interface for "Add Information"
            columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            columnPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        }
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnFirstName.setOnEditCommit(event -> {
            String queryText = String.format(
                    "UPDATE supervisor SET firstname = '%s' WHERE (id = %d)",
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
                    "UPDATE supervisor SET lastname = '%s' WHERE (id = %d)",
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
                // Operation failed
                alert(Alert.AlertType.ERROR, "Supervisor", "Email already exists");
                int index = supervisors.indexOf(event.getRowValue());
                if (index >= 0) {
                    supervisors.set(index, event.getRowValue());
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

        try {
            supervisors = getSupervisors();
            supervisorTable.setItems(supervisors);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private ObservableList<AdminSupervisorModel> getSupervisors() throws SQLException {
        String queryText = "SELECT * FROM supervisor";
        ResultSet response = getResponse(connection, queryText);
        ObservableList<AdminSupervisorModel> students = FXCollections.observableArrayList();
        ResultSet resultSet;
        while (response.next()) {
            resultSet = getResponse(connection, "SELECT email, password FROM credentials WHERE id = "+response.getInt("credentials_id"));
            resultSet.next();
            students.add(new AdminSupervisorModel(
                    response.getInt("id"), response.getInt("credentials_id"), response.getString("firstname"),
                    response.getString("lastname"), resultSet.getString("email"), resultSet.getString("password")
            ));
        }
        return students;
    }

    @FXML
    void addUser() {
        if (firstName.getText().isEmpty() | lastName.getText().isEmpty() | email.getText().isEmpty() | password.getText().isEmpty()) {
            alert(Alert.AlertType.ERROR, "Credentials", "All fields are required");
            return;
        }
        String queryText = String.format(
                "INSERT INTO credentials (email, password, user) VALUES ('%s', '%s', 2)",
                email.getText(), password.getText());
        try {
            executeQuery(connection, queryText);
            queryText = String.format("SELECT id FROM credentials WHERE email = '%s'", email.getText());
            ResultSet response = getResponse(connection, queryText);
            response.next();
            queryText = String.format(
                    "INSERT INTO supervisor (credentials_id, firstname, lastname) VALUES (%d, '%s', '%s')",
                    response.getInt("id"), firstName.getText(), lastName.getText());
            executeQuery(connection, queryText);
            int credentialID = response.getInt("id");
            queryText = String.format("SELECT id FROM supervisor WHERE credentials_id = %d", credentialID);
            response = getResponse(connection, queryText);
            response.next();
            supervisors.add(new AdminSupervisorModel(response.getInt("id"), credentialID, firstName.getText(), lastName.getText(), email.getText(), password.getText()));
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
        int index = supervisorTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Supervisor", "Select a column to delete");
            return;
        }
        AdminSupervisorModel supervisor = supervisors.get(index);
        Optional<ButtonType> option = alert(Alert.AlertType.CONFIRMATION, "Delete", "Are you sure you want to delete this record");
        option.ifPresent(buttonType -> {
            if (ButtonType.OK.equals(buttonType)) {
                try {
                    supervisors.remove(supervisor);
                    String queryText = String.format(
                            "DELETE FROM supervisor WHERE (credentials_id = %s)",
                            supervisor.getCredentialID()
                    );
                    executeQuery(connection, queryText);
                    queryText = String.format(
                            "DELETE FROM credentials WHERE (id = %d)",
                            supervisor.getCredentialID()
                    );
                    executeQuery(connection, queryText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

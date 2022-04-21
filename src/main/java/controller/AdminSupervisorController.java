package main.java.controller;

import main.java.ViewFactory;
import main.java.database.AppDatabase;
import main.java.model.AdminSupervisorModel;
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

import static main.java.Utils.*;
import static main.java.Utils.alert;

public class AdminSupervisorController extends BaseController {

    private final Connection connection;
    private ObservableList<AdminSupervisorModel> supervisors;
    private int id, credentialId;
    private final int type;

    @FXML
    private Button add, edit, update, delete;

    @FXML
    private Label optionTitle, firstNameTitle, lastNameTitle, emailTitle, passwordTitle;

    @FXML
    private TableView<AdminSupervisorModel> supervisorTable;

    @FXML
    private TableColumn<AdminSupervisorModel, String> columnId, columnFirstName, columnLastName, columnEmail, columnPassword;

    @FXML
    private TextField firstName, lastName, email, password;

    public AdminSupervisorController(ViewFactory viewFactory, String fxmlName, int type) {
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

            firstName.setVisible(false);
            lastName.setVisible(false);
            email.setVisible(false);
            password.setVisible(false);
            add.setVisible(false);
            edit.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
            AnchorPane.setRightAnchor(supervisorTable, 10.0);
        }
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        try {
            supervisors = getSupervisors();
            supervisorTable.setItems(supervisors);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        update.setDisable(true);
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

    @FXML
    void editUser() {
        int index = supervisorTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Supervisor", "Select a column to edit");
            return;
        }
        AdminSupervisorModel supervisor = supervisors.get(index);
        id = supervisor.getId();
        credentialId = supervisor.getCredentialID();
        firstName.setText(supervisor.getFirstname());
        lastName.setText(supervisor.getLastname());
        email.setText(supervisor.getLastname());
        password.setText(supervisor.getLastname());
        add.setDisable(true);
        update.setDisable(false);
        delete.setDisable(true);
    }

    @FXML
    void updateUser() {
        String queryText = String.format(
                "UPDATE supervisor SET firstname = '%s', lastname = '%s' WHERE (id = %d)",
                firstName.getText(), lastName.getText(), id
        );
        try {
            executeQuery(connection, queryText);
            queryText = String.format(
                    "UPDATE credentials SET email = '%s', password = '%s' WHERE (id = %d)",
                    email.getText(), password.getText(), credentialId
            );
            executeQuery(connection, queryText);
            supervisors.clear();
            supervisors.addAll(getSupervisors());
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

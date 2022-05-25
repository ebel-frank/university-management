package com.horizons.controller;

import com.horizons.FxmlMethods;
import com.horizons.database.AppDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {

    @FXML
    private Label errLabel;
    @FXML
    private TextField email, password;
    
    public void setUpVariables() {
    	email.setText("josh");
    	password.setText("12345");
    }

    /**
     * The method gets the values entered by the user to check if it matches any value in the credentials table
     * if it does match it sends the user to the welcome screen.
     */
    @FXML
    protected void loginBtnAction() {
        if (fieldsAreValid()) {
            Connection connection = AppDatabase.getConnection();

            String verifyCredentials =
                    "SELECT * FROM credentials WHERE email = '" + email.getText() + "' AND password = '" + password.getText() + "'";
            try {
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery(verifyCredentials);

                if (queryResult.next()) {
                    /*
                    user column contains integer where
                    0: Student
                    1: Professor
                    2: Supervisor
                    3: Administrator
                     */
                    int id = queryResult.getInt("id");
                    FxmlMethods fxmlMethods = FxmlMethods.getInstance();
                    switch (queryResult.getInt("user")) {
                        case 0 -> {
                            fxmlMethods.updateStage("student_welcome_application.fxml", (Stage) email.getScene().getWindow(), id);
                        }
                        case 1 -> {
                            fxmlMethods.updateStage("professor_welcome_application.fxml", (Stage) email.getScene().getWindow(), id);
                        }
                        case 2 -> {
                            fxmlMethods.updateStage("supervisor_welcome_application.fxml", (Stage) email.getScene().getWindow(), id);
                        }
                        default -> {
                            fxmlMethods.updateStage("admin_welcome_application.fxml", (Stage) email.getScene().getWindow(), id);
                        }
                    }
                } else {
                    errLabel.setText("Invalid email and password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method validates our user inputs
     * @return true if all inputs are valid and false otherwise
     */
    private boolean fieldsAreValid() {
        if (email.getText().isEmpty()) {
            errLabel.setText("Input your email address");
            return false;
        } else if (password.getText().isEmpty()) {
            errLabel.setText("Input your password");
            return false;
        }
        return true;
    }

}
package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController extends BaseController {

    @FXML
    private Label errLabel;
    @FXML
    private TextField email, password;

    public LoginController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }

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
                    int user = queryResult.getInt("user");
                    String optionText1, optionText2;
                    switch (user) {
                        case 0 -> {
                            optionText1 = "Modules";
                            optionText2 = "Grades";
                        }
                        case 1 -> {
                            optionText1 = "Students";
                            optionText2 = "Grades";
                        }
                        case 2 -> {
                            optionText1 = "Students";
                            optionText2 = "Professors";
                        }
                        default -> {
                            optionText1 = "View Information";
                            optionText2 = "Add Information";
                        }
                    }
                    BaseController controller = new WelcomeController(
                            viewFactory, "welcome_application.fxml", id,
                            user, optionText1, optionText2);
                    viewFactory.updateStage(controller, (Stage) email.getScene().getWindow());
                } else {
                    errLabel.setText("Invalid email and password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
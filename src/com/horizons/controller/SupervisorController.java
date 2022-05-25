package com.horizons.controller;

import com.horizons.FxmlMethods;
import com.horizons.database.AppDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.horizons.Utils.getResponse;

public class SupervisorController {

	private int supervisorId;
    private Connection connection;

    @FXML
    private AnchorPane mainView, contentView;

    @FXML
    private MenuButton profileMenu;

    @FXML
    private Label profileTitle;

    @FXML
    private RadioButton option1, option2;

    @FXML
    private ToggleGroup user;

    /**
     * This method is used to set up the variables
     * @param type	It tells our application to set up the interface of "Students" or "Professors"
     * @param id	The supervisor Id
     */
    public void setUpVariables(int type, int id) {
    	this.connection = AppDatabase.getConnection();
        this.supervisorId = id;
        
        if (type == 0) {
            try {
                FxmlMethods.getInstance().updateRoot(mainView, contentView, "supervisor_student.fxml", 0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // add a listener to the toggle group
            user.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            	// This block of code is executed any time the user clicks on a different radio button
                try {
                    String fxmlName;
                    if (((RadioButton) newToggle).getText().equals("All Students")) {
                    	fxmlName = "supervisor_student.fxml";
                    } else {
                    	fxmlName = "supervisor_grades.fxml";
                    }
                    FxmlMethods.getInstance().updateRoot(mainView, contentView, fxmlName, 0);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            });
        } else {
            try {
            	FxmlMethods.getInstance().updateRoot(mainView, contentView, "supervisor_professor.fxml", 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            option1.setText("Professors");
            option2.setVisible(false);
        }

        try {
            ResultSet response = getSupervisorDetails();
            response.next();
            profileTitle.setText(String.format("Supervisor. %s %s", response.getString("firstname"), response.getString("lastname")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return	the details of the supervisor 
     * @throws SQLException
     */
    private ResultSet getSupervisorDetails() throws SQLException {
        String queryText = "SELECT * FROM supervisor WHERE credentials_id = "+supervisorId;
        return getResponse(connection, queryText);
    }

    /**
     * This shows the logout dropdown-button 
     */
    @FXML
    void showLogout() {
        profileMenu.show();
    }

    /**
     * Logs the user out of the application
     */
    @FXML
    void logout() {
        Stage stage = (Stage) profileMenu.getScene().getWindow();
        FxmlMethods fxmlMethods = FxmlMethods.getInstance();
        fxmlMethods.closeStage(stage);
        fxmlMethods.showLoginWindow();
    }

    /**
     * Takes the user back to the welcome screen
     */
    @FXML
    void goBack() {
        Stage stage = (Stage) profileMenu.getScene().getWindow();
        FxmlMethods.getInstance().goBack(stage);
    }
}

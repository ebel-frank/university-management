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

public class AdminController {

	private int adminId;
    private Connection connection;

    @FXML
    private AnchorPane mainView, contentView;

    @FXML
    private MenuButton profileMenu;

    @FXML
    private Label profileTitle;

    @FXML
    private ToggleGroup user;

    /**
     * This method is used to set up the variables
     * @param type	It tells our application to set up the interface of "View Information" or "Add Information"
     * @param id	The administrator Id
     */
    public void setUpVariables(int type, int id) {
    	this.connection = AppDatabase.getConnection();
        this.adminId = id;
        
        try {
            FxmlMethods.getInstance().updateRoot(mainView, contentView, "admin_student.fxml", type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ResultSet response = getAdminDetails();
            response.next();
            profileTitle.setText(String.format("Admin. %s %s", response.getString("firstname"), response.getString("lastname")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // add a listener to the toggle group
        user.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            // This block of code is executed any time the user clicks on a different radio button
        	try {
                String fxmlName;
                switch (((RadioButton) newToggle).getText()) {
                    case "Students" -> fxmlName = "admin_student.fxml";
                    case "Supervisors" -> fxmlName = "admin_supervisor.fxml";
                    default -> fxmlName = "admin_professor.fxml";
                }
                FxmlMethods.getInstance().updateRoot(mainView, contentView, fxmlName, type);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * @return the details of the Administrator e.g firstname, lastname 
     * @throws SQLException
     */
    private ResultSet getAdminDetails() throws SQLException {
        String queryText = "SELECT * FROM administrator WHERE credentials_id = '"+adminId+"'";
        return getResponse(connection, queryText);
    }

    /**
     * shows the logout dropdown-button
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

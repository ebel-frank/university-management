package com.horizons.controller;

import com.horizons.ViewFactory;
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

public class AdminController extends BaseController{

    private final int type, adminId;
    private final Connection connection;

    @FXML
    private AnchorPane mainView, contentView;

    @FXML
    private MenuButton profileMenu;

    @FXML
    private Label profileTitle;

    @FXML
    private ToggleGroup user;

    /**
     * Constructor of the AdminController
     * @param viewFactory	The ViewFactory object which will manage the layout
     * @param fxmlName		The fxml name of the controller
     * @param type			It tells our application to set up the interface of "View Information" or "Add Information"
     * @param id			The administrator Id
     */
    public AdminController(ViewFactory viewFactory, String fxmlName, int type, int id) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
        this.adminId = id;
    }

    /**
     * This method is used to configure the variables
     */
    @FXML
    public void initialize() {
        try {
            viewFactory.updateRoot(mainView, contentView, new AdminStudentController(viewFactory, "admin_student.fxml", type));
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
                BaseController controller;
                switch (((RadioButton) newToggle).getText()) {
                    case "Students" -> controller = new AdminStudentController(viewFactory, "admin_student.fxml", type);
                    case "Supervisors" -> controller = new AdminSupervisorController(viewFactory, "admin_supervisor.fxml", type);
                    default -> controller = new AdminProfessorController(viewFactory, "admin_professor.fxml", type);
                }
                viewFactory.updateRoot(mainView, contentView, controller);
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
        viewFactory.closeStage(stage);
        viewFactory.showLoginWindow();
    }

    /**
     * Takes the user back to the welcome screen
     */
    @FXML
    void goBack() {
        Stage stage = (Stage) profileMenu.getScene().getWindow();
        viewFactory.goBack(stage);
    }

}

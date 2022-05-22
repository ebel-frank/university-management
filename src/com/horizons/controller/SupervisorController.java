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

public class SupervisorController extends BaseController {

    private final int type, supervisorId;
    private final Connection connection;

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

    public SupervisorController(ViewFactory viewFactory, String fxmlName,
                                int type, int id) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
        this.supervisorId = id;
    }

    @FXML
    public void initialize() {
        if (type == 0) {
            try {
                viewFactory.updateRoot(mainView, contentView, new SupervisorStudentController(viewFactory, "supervisor_student.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // add a listener to the toggle group
            user.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            	// This block of code is executed any time the user clicks on a different radio button
                try {
                    BaseController controller;
                    if (((RadioButton) newToggle).getText().equals("All Students")) {
                        controller = new SupervisorStudentController(viewFactory, "supervisor_student.fxml");
                    } else {
                        controller = new SupervisorGradesController(viewFactory, "supervisor_grades.fxml");
                    }
                    viewFactory.updateRoot(mainView, contentView, controller);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            });
        } else {
            try {
                viewFactory.updateRoot(mainView, contentView, new SupervisorProfessorController(viewFactory, "supervisor_professor.fxml"));
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

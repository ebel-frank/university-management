package main.java.controller;

import main.java.ViewFactory;
import main.java.database.AppDatabase;
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

import static main.java.Utils.getResponse;

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

            user.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
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

    private ResultSet getSupervisorDetails() throws SQLException {
        String queryText = "SELECT * FROM supervisor WHERE credentials_id = "+supervisorId;
        return getResponse(connection, queryText);
    }

    @FXML
    void showLogout() {
        profileMenu.show();
    }

    @FXML
    void logout() {
        Stage stage = (Stage) profileMenu.getScene().getWindow();
        viewFactory.closeStage(stage);
        viewFactory.showLoginWindow();
    }
}

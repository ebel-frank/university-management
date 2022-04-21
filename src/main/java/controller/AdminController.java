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

import static main.java.Utils.getResponse;

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

    public AdminController(ViewFactory viewFactory, String fxmlName, int type, int id) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.type = type;
        this.adminId = id;
    }

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

        user.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
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

    private ResultSet getAdminDetails() throws SQLException {
        String queryText = "SELECT * FROM administrator WHERE credentials_id = '"+adminId+"'";
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

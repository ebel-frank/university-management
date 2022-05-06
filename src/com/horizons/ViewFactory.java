package com.horizons;

import com.horizons.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewFactory {

    private Stage welcomeStage = null;

    public void showLoginWindow() {
        BaseController controller = new LoginController(this, "login_application.fxml");
        initStage(controller, "Login", 953, 503);
    }

    public void showAdminWindow(int type, int id) {
        BaseController controller = new AdminController(
                this, "admin_application.fxml", type, id);
        initStage(controller);
    }

    public void showStudentWindow(int type, int id) {
        BaseController controller = new StudentController(
                this, "student_application.fxml",
                type, id);
        initStage(controller);
    }

    public void showProfessorWindow(int type, int id) {
        BaseController controller = new ProfessorController(
                this, "professor_application.fxml",
                type, id);
        initStage(controller);
    }

    public void showSupervisorWindow(int type, int id) {
        BaseController controller = new SupervisorController(
                this, "supervisor_application.fxml",
                type, id);
        initStage(controller);
    }

    private void initStage(BaseController controller, String title, int width, int height) {
        System.out.println();
        Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream("resources/Koulen.ttf")),
                10
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        loader.setController(controller);
        Parent parent;
        try {
            parent = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle(title);

        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setResizable(false);
        stage.show();
        if (controller instanceof LoginController) {
            welcomeStage = stage;
        }
    }

    private void initStage(BaseController controller) {
        initStage(controller, "University Management", 953, 606);
    }

    public void updateStage(BaseController controller, Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        loader.setController(controller);
        Parent parent;
        try {
            parent = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        stage.getScene().setRoot(parent);
    }

    public void updateRoot(AnchorPane mainView, AnchorPane contentView, BaseController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        loader.setController(controller);
        contentView = loader.load();
        AnchorPane.setTopAnchor(contentView, 41.0);
        mainView.getChildren().set(1, contentView);
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public void goBack(Stage stage) {
        closeStage(stage);
        welcomeStage.show();
    }
}

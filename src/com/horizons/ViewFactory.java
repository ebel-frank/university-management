package com.horizons;

import com.horizons.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The ViewFactory class manages our layouts
 *
 */
public class ViewFactory {

    private Stage welcomeStage = null;

    /**
     * This method displays the Login Window
     */
    public void showLoginWindow() {
        BaseController controller = new LoginController(this, "login_application.fxml");
        initStage(controller, "Login", 953, 503);
    }

    /**
     * This method displays the Administrator Window
     * @param type 	It tells our application to set up the interface of "View Information" or "Add Information"
     * @param id 	The administrator's id
     */
    public void showAdminWindow(int type, int id) {
        BaseController controller = new AdminController(
                this, "admin_application.fxml", type, id);
        initStage(controller);
    }

    /**
     * This method displays the Student Window
     * @param type 	It tells our application to set up the interface of "Modules" or "Grades"
     * @param id 	The student's id
     */
    public void showStudentWindow(int type, int id) {
        BaseController controller = new StudentController(
                this, "student_application.fxml",
                type, id);
        initStage(controller);
    }

    /**
     * This method displays the Professor Window
     * @param type	It tells our application to set up the interface of "Students" or "Grades"
     * @param id	The professor's id
     */
    public void showProfessorWindow(int type, int id) {
        BaseController controller = new ProfessorController(
                this, "professor_application.fxml",
                type, id);
        initStage(controller);
    }

    /**
     * This method displays the Supervisor Window
     * @param type	It tells our application to set up the interface of "Students" or "Professors"
     * @param id	The supervisor's id
     */
    public void showSupervisorWindow(int type, int id) {
        BaseController controller = new SupervisorController(
                this, "supervisor_application.fxml",
                type, id);
        initStage(controller);
    }
    
    /**
     * This method displays the Transcript Window
     * @param controller	The controller of the transcript.fxml
     */
    public void showTranscriptWindow(BaseController controller) {
        initStage(controller, "Transcript", 529, 509);
    }

    /**
     * This setup the stage with the fxml and displays it to the user
     * @param controller 	The controller of the stage
     * @param title			The title of the stage
     * @param width			The width of the stage
     * @param height		The height of the stage
     */
    private void initStage(BaseController controller, String title, int width, int height) {
        // Add the Koulen font to this application
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
        
        if (controller instanceof LoginController) {
            welcomeStage = stage;
        }
        if (controller instanceof TranscriptController) {
        	// set the stage of the Transcript controller to be a child of it's ownerStage
        	// in this case: the ownerStage is the stage of the Student Controller
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((TranscriptController) controller).ownerStage);
        }

        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setResizable(false);
        stage.show();
        
    }

    /**
     * This setup the stage with the fxml and displays it to the user
     * @param controller	The controller of the stage
     */
    private void initStage(BaseController controller) {
        initStage(controller, "University Management", 953, 606);
    }

    /**
     * This method update the view elements in a given stage
     * @param controller	The controller of the stage
     * @param stage			The stage we want to update
     */
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

    /**
     * This method update the view elements of a given AnchorPane.
     * It is similar to the updateStage() method. But it's used when we want to 
     * update the view elements of only a section of the stage.
     * @param mainView		The parent of the contentView
     * @param contentView	The AnchorPane which is to be updated
     * @param controller	The controller of the contentView
     */
    public void updateRoot(AnchorPane mainView, AnchorPane contentView, BaseController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        loader.setController(controller);
        contentView = loader.load();
        AnchorPane.setTopAnchor(contentView, 41.0);
        mainView.getChildren().set(1, contentView);
    }

    /**
     * This closes a stage
     * @param stage
     */
    public void closeStage(Stage stage) {
        stage.close();
    }

    public void goBack(Stage stage) {
        closeStage(stage);
        welcomeStage.show();
    }
}

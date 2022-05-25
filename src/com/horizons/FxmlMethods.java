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
 * This is a singleton class which manages our layouts
 */
public class FxmlMethods {

    private Stage welcomeStage = null;
    private static FxmlMethods instance = null;
    
    private FxmlMethods(){}
    
    public static synchronized FxmlMethods getInstance() {
    	if(instance == null) {
    		instance = new FxmlMethods();
    	}
    	return instance;
    }

    /**
     * This method displays the Login Window
     */
    public void showLoginWindow() {
        initStage("login_application.fxml", "Login", 953, 503);
    }

    /**
     * This method displays the Administrator Window
     * @param type 	It tells our application to set up the interface of "View Information" or "Add Information"
     * @param id 	The administrator's id
     */
    public void showAdminWindow(int type, int id) { 
        initStage("admin_application.fxml", type, id);
    }

    /**
     * This method displays the Student Window
     * @param type 	It tells our application to set up the interface of "Modules" or "Grades"
     * @param id 	The student's id
     */
    public void showStudentWindow(int type, int id) {
        initStage("student_application.fxml",type, id);
    }

    /**
     * This method displays the Professor Window
     * @param type	It tells our application to set up the interface of "Students" or "Grades"
     * @param id	The professor's id
     */
    public void showProfessorWindow(int type, int id) {      
        initStage("professor_application.fxml",type, id);
    }

    /**
     * This method displays the Supervisor Window
     * @param type	It tells our application to set up the interface of "Students" or "Professors"
     * @param id	The supervisor's id
     */
    public void showSupervisorWindow(int type, int id) {
        initStage("supervisor_application.fxml", type, id);
    }
    
    /**
     * This method displays the Transcript Window
     * @param controller	The controller of the transcript.fxml
     */
    public void showTranscriptWindow(String fxmlName, String fullname,
            int year, String specialty, int studentId, Stage ownerStage) {
        //initStage(controller, , , );
    	Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream("resources/Koulen.ttf")),
                10
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/"+fxmlName));
        Parent parent;
        try {
            parent = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        ((TranscriptController) loader.getController()).setUpVariables(fullname, year, specialty, studentId);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Transcript");
        
    	// set the stage of the Transcript controller to be a child of it's ownerStage
    	// in this case: the ownerStage is the stage of the Student Controller
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ownerStage);
        
        stage.setMinWidth(529);
        stage.setMinHeight(509);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This setup the stage with the fxml and displays it to the user
     * @param fxmlName 	The fxml name of the controller
     * @param title		The title of the stage
     * @param width		The width of the stage
     * @param height	The height of the stage
     */
    private void initStage(String fxmlName, String title, int width, int height,
    		int type, int id) {
        // Add the Koulen font to this application
    	Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream("resources/Koulen.ttf")),
                10
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/"+fxmlName));
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
        
        Object controller = loader.getController();
        if (controller instanceof LoginController) {
            welcomeStage = stage;
            ((LoginController) controller).setUpVariables();
        } else if (controller instanceof AdminController) {
        	((AdminController) controller).setUpVariables(type, id);
        }else if (controller instanceof SupervisorController) {
        	((SupervisorController) controller).setUpVariables(type, id);
        }else if (controller instanceof ProfessorController) {
        	((ProfessorController) controller).setUpVariables(type, id);
        }else if (controller instanceof StudentController) {
        	((StudentController) controller).setUpVariables(type, id);
        }

        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setResizable(false);
        stage.show();
        
    }

    /**
     * This setup the stage with the fxmlName and displays it to the user
     */
    private void initStage(String fxmlName, int type, int id) {
        initStage(fxmlName, "University Management", 953, 606, type, id);
    }
    
    /**
     * This setup the stage with the fxmlName and displays it to the user
     */
    private void initStage(String fxmlName, String title, int width, int height) {
        initStage(fxmlName, title, width, height, 0, 0);
    }

    /**
     * This method update the view elements in a given stage
     * @param fxmlName	The controller of the stage
     * @param stage		The stage we want to update
     * @param id		The id of the user
     */
    public void updateStage(String fxmlName, Stage stage, int id) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/"+fxmlName));
        // loader.setController(controller);
        Parent parent;
        try {
            parent = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (loader.getController() instanceof StudentWelcomeController) {
        	((StudentWelcomeController) loader.getController()).setId(id);
        } else if (loader.getController() instanceof ProfessorWelcomeController) {
        	((ProfessorWelcomeController) loader.getController()).setId(id);
        } else if (loader.getController() instanceof SupervisorWelcomeController) {
        	((SupervisorWelcomeController) loader.getController()).setId(id);
        } else if (loader.getController() instanceof AdminWelcomeController) {
        	((AdminWelcomeController) loader.getController()).setId(id);
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
    public void updateRoot(AnchorPane mainView, AnchorPane contentView, String fxmlName, int type) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/"+fxmlName));
        contentView = loader.load();
        AnchorPane.setTopAnchor(contentView, 41.0);
        mainView.getChildren().set(1, contentView);
        if (loader.getController() instanceof AdminStudentController) {
        	((AdminStudentController) loader.getController()).setUpVariables(type);
        	
        } else if (loader.getController() instanceof AdminSupervisorController) {
        	((AdminSupervisorController) loader.getController()).setUpVariables(type);
        	
        } else if (loader.getController() instanceof AdminProfessorController) {
        	((AdminProfessorController) loader.getController()).setUpVariables(type);
        	
        } else if (loader.getController() instanceof SupervisorStudentController) {
        	((SupervisorStudentController) loader.getController()).setUpVariables();
        } else if (loader.getController() instanceof SupervisorGradesController) {
        	((SupervisorGradesController) loader.getController()).setUpVariables();
        } else if (loader.getController() instanceof SupervisorProfessorController) {
        	((SupervisorProfessorController) loader.getController()).setUpVariables();
        }
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

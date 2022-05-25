package com.horizons.controller;

import com.horizons.FxmlMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StudentWelcomeController {
    @FXML
    private Button option1, option2;

    private int id;
    
    public void setId(int id) {
    	this.id = id;
    }

    /**
     * This method is called by the two buttons in the welcome screen. It directs
     * the user to the proper interface.
     * @param event
     */
    @FXML
    void options(ActionEvent event) {
        int type;
        Stage stage = (Stage) option1.getScene().getWindow();
        if(option1 == event.getTarget()) {
            type = 0;
        } else {
            type = 1;
        }
        FxmlMethods fxmlMethods = FxmlMethods.getInstance();
        fxmlMethods.showStudentWindow(type, id);
        fxmlMethods.closeStage(stage);
    }
}

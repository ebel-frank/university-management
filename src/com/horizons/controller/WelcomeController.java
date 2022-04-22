package com.horizons.controller;

import com.horizons.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WelcomeController extends BaseController {
    @FXML
    private Button option1, option2;

    private int id, user;
    private String optionText1, optionText2;

    public WelcomeController(ViewFactory viewFactory, String fxmlName, int id,
                             int user, String optionText1, String optionText2) {
        super(viewFactory, fxmlName);
        this.id = id;
        this.user = user;
        this.optionText1 = optionText1;
        this.optionText2 = optionText2;
    }

    @FXML
    public void initialize() {
        option1.setText(optionText1);
        option2.setText(optionText2);
    }

    @FXML
    void options(ActionEvent event) {
        int type;
        Stage stage = (Stage) option1.getScene().getWindow();
        if(option1 == event.getTarget()) {
            type = 0;
        } else {
            type = 1;
        }
        switch (user) {
            case 0 -> {
                viewFactory.showStudentWindow(type, id);
            }
            case 1 -> {
                viewFactory.showProfessorWindow(type, id);
            }
            case 2 -> {
                viewFactory.showSupervisorWindow(type, id);
            }
            case 3 -> {
                viewFactory.showAdminWindow(type, id);
            }
        }
        viewFactory.closeStage(stage);
    }
}

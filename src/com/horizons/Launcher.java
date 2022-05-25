package com.horizons;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) {
    	// get an instance of the FxmlMethods class and show the login screen
        FxmlMethods fxmlMethods = FxmlMethods.getInstance();
        fxmlMethods.showLoginWindow();
    }

    /**
     * This is the entry point of this application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
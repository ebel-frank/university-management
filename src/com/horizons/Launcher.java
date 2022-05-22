package com.horizons;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) {
    	// create an instance of the ViewFactory class and show the login screen
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showLoginWindow();
    }

    /**
     * This is the entry point of this application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
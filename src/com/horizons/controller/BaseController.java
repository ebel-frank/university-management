package com.horizons.controller;

import com.horizons.ViewFactory;

/**
 * This class is the base class for all the other controllers from which they all
 * extends. It holds reference to the ViewFactory object and the fxml name.
 */
public abstract class BaseController {
    private final String fxmlName;
    protected ViewFactory viewFactory;

    /**
     * The constructor of the BaseController
     * @param viewFactory	The ViewFactory object which will manage the layout
     * @param fxmlName		The fxml name of the controller that extends this class
     */
    public BaseController(ViewFactory viewFactory, String fxmlName) {
        this.viewFactory = viewFactory;
        this.fxmlName = "resources/"+fxmlName;
    }

    /**
     * @return the fxml name for the controller
     */
    public String getFxmlName() {
        return fxmlName;
    }
}

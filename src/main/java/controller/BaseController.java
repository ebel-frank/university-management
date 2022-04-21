package main.java.controller;

import main.java.ViewFactory;

public abstract class BaseController {
    private final String fxmlName;
    protected ViewFactory viewFactory;

    public BaseController(ViewFactory viewFactory, String fxmlName) {
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    public String getFxmlName() {
        return fxmlName;
    }
}

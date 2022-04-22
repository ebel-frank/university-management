package com.horizons.controller;

import com.horizons.ViewFactory;

public abstract class BaseController {
    private final String fxmlName;
    protected ViewFactory viewFactory;

    public BaseController(ViewFactory viewFactory, String fxmlName) {
        this.viewFactory = viewFactory;
        this.fxmlName = "resources/"+fxmlName;
    }

    public String getFxmlName() {
        return fxmlName;
    }
}

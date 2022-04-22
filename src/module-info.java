module UniversityManagement {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
    requires javafx.web;
    
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
	
    opens com.horizons.controller;
    opens com.horizons.model;
	opens com.horizons to javafx.graphics, javafx.fxml;
}

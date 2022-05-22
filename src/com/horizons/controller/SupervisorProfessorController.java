package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.SupervisorProfessorModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.horizons.Utils.*;

public class SupervisorProfessorController extends BaseController {

    private final Connection connection;

    @FXML
    private TableView<SupervisorProfessorModel> professorTable;

    @FXML
    private TableColumn<SupervisorProfessorModel, String> columnFirstName, columnLastName, columnSubject;

    @FXML
    private TableColumn<?, Void> serialNo;

    public SupervisorProfessorController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
    }

    @FXML
    void initialize() {
        preventColumnReordering(professorTable);
        serialNo.setCellFactory(indexCellFactory());
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));

        try {
            professorTable.setItems(getProfessors());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns the list of all the professors which will be used by the 
     * table view in the SupervisorProfessorController.
     * @return
     * @throws SQLException
     */
    private ObservableList<SupervisorProfessorModel> getProfessors() throws SQLException {
        String queryText = "SELECT firstname, lastname, subject FROM professor INNER JOIN subject ON professor.subject_id = subject.id";
        ResultSet response = getResponse(connection, queryText);
        ObservableList<SupervisorProfessorModel> professors = FXCollections.observableArrayList();
        while (response.next()) {
            professors.add(new SupervisorProfessorModel(
                    response.getString("firstname"), response.getString("lastname"),
                    response.getString("subject")
            ));
        }
        return professors;
    }

}

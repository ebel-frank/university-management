package main.java.controller;

import main.java.ViewFactory;
import main.java.database.AppDatabase;
import main.java.model.SupervisorProfessorModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static main.java.Utils.*;

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

package com.horizons.controller;

import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import com.horizons.model.AdminStudentModel;
import com.horizons.model.SupervisorStudentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.horizons.Utils.*;

public class SupervisorStudentController extends BaseController {

    private final Connection connection;
    private ObservableList<SupervisorStudentModel> students;

    @FXML
    private TableView<SupervisorStudentModel> allStudentTable;

    @FXML
    private TableColumn<SupervisorStudentModel, String> columnFirstName, columnLastName, columnYear, columnSpecialty;

    @FXML
    private TableColumn<?, Void> serialNo;

    public SupervisorStudentController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
    }

    @FXML
    void initialize() {
        preventColumnReordering(allStudentTable);
        serialNo.setCellFactory(indexCellFactory());
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        columnSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        columnSpecialty.setCellFactory(ChoiceBoxTableCell.forTableColumn("TC", "SIC","GE","GME"));
        columnSpecialty.setOnEditCommit(event -> {
            int year = 2;
            String newValue = event.getNewValue();
            if (!Objects.equals(newValue, event.getRowValue().getSpecialty())) {
                if (Objects.equals(newValue, "TC")) {
                    year = 1;
                }
                String queryText = String.format(
                        "UPDATE student SET year = %d, specialty = '%s' WHERE (id = %d)",
                        year, event.getNewValue(), event.getRowValue().getId()
                );
                event.getRowValue().setSpecialty(event.getNewValue());
                event.getRowValue().setYear(year);
                new Thread(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        executeQuery(connection, queryText);

                        unregisterStudentForCourses(connection, event.getRowValue().getId());
                        registerStudentForCourses(connection, event.getRowValue().getSpecialty(), event.getRowValue().getId());
                        return null;
                    }
                }).start();
                int index = students.indexOf(event.getRowValue());
                if (index >= 0) {
                    students.set(index, event.getRowValue());
                }
            }
        });

        try {
            students = getStudents();
            allStudentTable.setItems(students);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private ObservableList<SupervisorStudentModel> getStudents() throws SQLException {
        String queryText = "SELECT * FROM student ORDER BY year ASC";
        ResultSet response = getResponse(connection, queryText);
        ObservableList<SupervisorStudentModel> students = FXCollections.observableArrayList();
        while (response.next()) {
            students.add(new SupervisorStudentModel(
                    response.getInt("id"), response.getString("firstname"), response.getString("lastname"),
                    response.getInt("year"), response.getString("specialty")
            ));
        }
        return students;
    }

}

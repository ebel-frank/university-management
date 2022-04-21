package main.java.controller;

import main.java.ViewFactory;
import main.java.database.AppDatabase;
import main.java.model.AdminStudentModel;
import main.java.model.SupervisorStudentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static main.java.Utils.*;

public class SupervisorStudentController extends BaseController {

    private final Connection connection;
    private ObservableList<SupervisorStudentModel> students;
    private int id;

    @FXML
    private Button edit, update;

    @FXML
    private TableView<SupervisorStudentModel> allStudentTable;

    @FXML
    private TableColumn<AdminStudentModel, String> columnId, columnFirstName, columnLastName, columnYear, columnSpecialty;

    @FXML
    private TextField fullName;

    @FXML
    private ChoiceBox<String> year, specialty;

    public SupervisorStudentController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
    }

    @FXML
    void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        columnSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));

        try {
            students = getStudents();
            allStudentTable.setItems(students);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        year.getItems().addAll("1", "2");
        year.setOnAction(event -> {
            specialty.getItems().clear();
            if(year.getValue().equals("1")) {
                specialty.getItems().add("TC");
                specialty.setValue("TC");
                return;
            }
            specialty.getItems().addAll("SIC","GE","GME");
            specialty.setValue("SIC");
        });
        year.setValue("1");
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

    @FXML
    void editUser() {
        int index = allStudentTable.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            alert(Alert.AlertType.ERROR, "Student", "Select a column to edit");
            return;
        }
        SupervisorStudentModel student = students.get(index);
        id = student.getId();
        fullName.setText(student.getFirstname()+" "+student.getLastname());
        year.setValue(student.getYear()+"");
        specialty.setValue(student.getSpecialty());
        edit.setDisable(true);
        update.setDisable(false);
    }

    @FXML
    void updateUser() {
        String queryText = String.format(
                "UPDATE student SET year = %s, specialty = '%s' WHERE (id = %d)",
                year.getValue(), specialty.getValue(), id
        );
        try {
            executeQuery(connection, queryText);
            unregisterStudentForCourses(connection, id);
            registerStudentForCourses(connection, specialty.getValue(), id);
            students.clear();
            students.addAll(getStudents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fullName.clear();
        update.setDisable(true);
        edit.setDisable(false);
    }

}

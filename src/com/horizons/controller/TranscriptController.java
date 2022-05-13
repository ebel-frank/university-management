package com.horizons.controller;

import com.horizons.Utils;
import com.horizons.ViewFactory;
import com.horizons.database.AppDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranscriptController extends BaseController {

    private final Connection connection;
    private final String fullname, specialty;
    private final int year, studentId;
    public Stage ownerStage;

    @FXML
    private Label nameTitle, yearTitle, specialtyTitle;

    @FXML
    private SpreadsheetView table;

    @FXML
    private AnchorPane transcriptPane;

    @FXML
    private ChoiceBox<String> semester;

    public TranscriptController(ViewFactory viewFactory, String fxmlName, String fullname,
                                int year, String specialty, int studentId, Stage ownerStage) {
        super(viewFactory, fxmlName);
        this.connection = AppDatabase.getConnection();
        this.fullname = fullname;
        this.year = year;
        this.specialty = specialty;
        this.studentId = studentId;
        this.ownerStage = ownerStage;

    }

    @FXML
    void initialize() {
        nameTitle.setText("Name: " + fullname);
        yearTitle.setText("Year: " + year);
        specialtyTitle.setText("Specialty: " + specialty);

        semester.getItems().addAll("Semester 1", "Semester 2");
        semester.setOnAction(event -> {
            if (semester.getValue().charAt(9) == '1') {
                table.setGrid(getGrades(0));
            } else {
                table.setGrid(getGrades(1));
            }
        });
        semester.setValue(semester.getItems().get(0));

        table.setEditable(false);
        table.setShowColumnHeader(false);
        table.setShowRowHeader(false);
        table.getColumns().get(0).setPrefWidth(70);
        table.getColumns().get(1).setPrefWidth(150);
        table.getColumns().get(4).setPrefWidth(80);
    }

    private GridBase getGrades(int index) {
        int rowCount = 10;
        int columnCount = 5;
        GridBase grid = new GridBase(rowCount, columnCount);

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();

        // Set up the headers
        list.add(SpreadsheetCellType.STRING.createCell(0, 0, 2, 1, "Module"));
        list.add(SpreadsheetCellType.STRING.createCell(0, 1, 1, 1, "Subject"));
        list.add(SpreadsheetCellType.STRING.createCell(0, 2, 1, 1, "Subject Grade"));
        list.add(SpreadsheetCellType.STRING.createCell(0, 3, 1, 1, "Module Grade"));
        list.add(SpreadsheetCellType.STRING.createCell(0, 4, 1, 1, "Situation"));
        rows.add(list);

        String queryText = "SELECT subject, module, ((exam * examCoeff * 0.01) + (tp * tpCoeff * 0.01) + (cc * ccCoeff * 0.01)) as total " +
                "FROM grades INNER JOIN courses ON courses.id = grades.courses_id INNER JOIN subject ON subject.id = courses.subject_id " +
                "INNER JOIN professor ON subject.id = professor.subject_id WHERE student_id = " + studentId + " AND semester = " + (index + (2 * year) - 1);
        try {
            ResultSet resultSet = Utils.getResponse(connection, queryText);
            int module = 1, count = 0, row = 0;
            double total = 0, semesterTotal = 0;
            SpreadsheetCell moduleCell = null;
            SpreadsheetCell moduleGradeCell = null;
            SpreadsheetCell situationCell = null;
            while (resultSet.next()) {
                if (resultSet.getInt("module") > module) {
                    double moduleGrade = total / count;
                    semesterTotal += moduleGrade;
                    module = resultSet.getInt("module");
                    // reset the module cell
                    moduleCell = null;

                    // reset the module Grade cell
                    assert moduleGradeCell != null;
                    moduleGradeCell.setItem(moduleGradeCell.getCellType().convertValue(String.format("%.2f", moduleGrade)));
                    moduleGradeCell = null;

                    assert situationCell != null;
                    if (moduleGrade >= 10) {
                        situationCell.setItem(situationCell.getCellType().convertValue("V"));
                    } else {
                        situationCell.setItem(situationCell.getCellType().convertValue("R"));
                    }
                    situationCell = null;

                    total = 0.0;
                    count = 0;
                }
                switch (resultSet.getInt("module")) {
                    case 1 -> {
                        if (moduleCell == null) {
                            moduleCell = SpreadsheetCellType.STRING.createCell(row, 0, 3, 1, "Module 1");
                            moduleGradeCell = SpreadsheetCellType.STRING.createCell(row, 3, 3, 1, total+"");
                            situationCell = SpreadsheetCellType.STRING.createCell(row, 4, 3, 1, "V");
                        }
                    }
                    case 2 -> {
                        if (moduleCell  == null) {
                            moduleCell = SpreadsheetCellType.STRING.createCell(row, 0, 3, 1, "Module 2");
                            moduleGradeCell = SpreadsheetCellType.STRING.createCell(row, 3, 3, 1, total+"");
                            situationCell = SpreadsheetCellType.STRING.createCell(row, 4, 3, 1, "V");
                        }
                    }
                    case 3 -> {
                        if (moduleCell  == null) {
                            moduleCell = SpreadsheetCellType.STRING.createCell(row, 0, 3, 1, "Module 3");
                            moduleGradeCell = SpreadsheetCellType.STRING.createCell(row, 3, 3, 1, total+"");
                            situationCell = SpreadsheetCellType.STRING.createCell(row, 4, 3, 1, "V");
                        }
                    }
                }
                final ObservableList<SpreadsheetCell> item = FXCollections.observableArrayList();
                item.add(moduleCell);
                item.add(SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, resultSet.getString("subject")));
                item.add(SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, resultSet.getString("total")));
                total += resultSet.getDouble("total");
                item.add(moduleGradeCell);
                item.add(situationCell);
                row++;
                count++;
                rows.add(item);
            }
            {
                double moduleGrade = total / count ;
                semesterTotal += moduleGrade;
                assert moduleGradeCell != null;
                moduleGradeCell.setItem(moduleGradeCell.getCellType().convertValue(String.format("%.2f", moduleGrade)));

                assert situationCell != null;
                if (moduleGrade >= 10) {
                    situationCell.setItem(situationCell.getCellType().convertValue("V"));
                } else {
                    situationCell.setItem(situationCell.getCellType().convertValue("R"));
                }
            }
            final ObservableList<SpreadsheetCell> bottomItems = FXCollections.observableArrayList();
            bottomItems.add(SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, "Total Grade for "+semester.getValue()));
            bottomItems.add(SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, ""));
            bottomItems.add(SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, ""));
            bottomItems.add(SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, String.format("%.2f", semesterTotal/3)));
            if ((semesterTotal/3) >= 10) {
                bottomItems.add(SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "V"));
            } else {
                bottomItems.add(SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "R"));
            }

            rows.add(bottomItems);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        grid.setRows(rows);
        grid.spanColumn(3, rows.size()-1, 0);
//        grid.spanRow(2, 3, 0);
//        grid.spanRow(3, 5, 0);
        return grid;
    }

    @FXML
    void download() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.printPage(transcriptPane);
            job.endJob();
        }
    }
}

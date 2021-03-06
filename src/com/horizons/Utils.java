package com.horizons;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Utils {
    /**
     * This returns the result when the database is queried using the queryText
     * @param connection SQL connection
     * @param queryText SQL statements
     * @return ResultSet object
     * @throws SQLException when queryText is incorrect
     */
    public static ResultSet getResponse(Connection connection, String queryText) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(queryText);
    }

    /**
     * This queries database using the queryText
     * @param connection SQL connection
     * @param queryText SQL statements
     * @throws SQLException when queryText is incorrect
     */
    public static void executeQuery(Connection connection, String queryText) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(queryText);
    }

    /**
     * @param type the alert type e.g ERROR, WARNING, INFORMATION
     * @param title the title of the alert
     * @param message the message of the alert
     */
    public static Optional<ButtonType> alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    /**
     * This registers the student to their various courses using the
     * student's specialty and studentId
     */
    public static void registerStudentForCourses(Connection connection, String specialty, int studentId) {
        String queryText = switch (specialty) {
            case "TC" -> "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 34);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 35);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 36);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 37);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 38);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 39);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 40);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 41);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 42);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 43);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 44);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 45);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 46);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 47);";
            case "SIC" -> "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 48);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 49);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 50);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 51);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 52);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 53);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 54);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 55);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 56);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 57);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 58);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 59);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 60);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 61);";
            case "GME" -> "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 62);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 63);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 64);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 65);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 66);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 67);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 68);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 69);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 70);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 71);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 72);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 73);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 74);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 75);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 76);";
            default -> "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 77);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 78);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 79);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 80);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 81);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 82);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 83);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 84);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 85);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 86);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 87);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 88);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 89);" +
                    "INSERT INTO grades (student_id, courses_id) VALUES (%1$d, 90);";
        };
        try {
            executeQuery(connection, String.format(queryText, studentId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unregisterStudentForCourses(Connection connection, int studentId) {
        String queryText = "DELETE FROM grades WHERE student_id = "+studentId;
        try {
            executeQuery(connection, queryText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is used for the serial number in a TableColumn
     * @param <T>
     * @return Callback object
     */
    public static <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> indexCellFactory() {
        return  t -> new TableCell<>() {
            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
                setText(isEmpty() ? "" : Integer.toString(i + 1));
            }
        };
    }

    /**
     * This is used for the serial number in a TreeTableColumn
     * @param <T>
     * @return Callback object
     */
    public static <T> Callback<TreeTableColumn<T, Void>, TreeTableCell<T, Void>> treeIndexCellFactory() {
        return  t -> new TreeTableCell<>() {
            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
                setText(isEmpty() ? "" : Integer.toString(i + 1));
            }
        };
    }

    /**
     * This method prevents the tableview from reordering its columns
     * @param tableView it could be a TreeTableView object or a TableView object
     */
    public static <T> void preventColumnReordering(Control tableView) {
        Platform.runLater(() -> {
            for (Node header: tableView.lookupAll(".column-header")) {
                header.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
            }
        });
    }

    /**
     * A Custom Row factory that makes the dropdown row not editable,
     * and it's children rows editable
     * @param table TreeTableView Object
     * @return  A custom row factory
     */
    public static <T> Callback<TreeTableView<T>, TreeTableRow<T>> getTreeTableViewRowFactory(TreeTableView<T> table) {
        return new Callback<>() {
            @Override
            public TreeTableRow<T> call(TreeTableView<T> param) {
                return new TreeTableRow<>() {
                    @Override
                    public void updateItem(T obj, boolean empty) {
                        super.updateItem(obj, empty);
                        boolean isTopLevel = table.getRoot().getChildren().contains(treeItemProperty().get());
                        setEditable(isEmpty() || !isTopLevel);
                    }
                };
            }
        };
    }

}

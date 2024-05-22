package org.example.lab_4.Models.Tables;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.lab_4.Models.FilteredTableRow;
import org.example.lab_4.Models.Group;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilteredAttendanceTable extends AbstractTable<FilteredTableRow> {
    public FilteredAttendanceTable(List<FilteredTableRow> rows) {
        table = new TableView<>();
        columns = new ArrayList<>();
        setupColumns(rows);
    }
    @Override
    public void setupColumns(List<FilteredTableRow> objects) {
        TableColumn<FilteredTableRow, String> studentNameColumn = new TableColumn<>("Vardas");
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<FilteredTableRow, String> studentSurnameColumn = new TableColumn<>("Pavardė");
        studentSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("studentSurname"));

        TableColumn<FilteredTableRow, String> groupNameColumn = new TableColumn<>("Grupė");
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));

        TableColumn<FilteredTableRow, LocalDate> dateColumn = new TableColumn<>("Data");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<FilteredTableRow, String> statusColumn = new TableColumn<>("Buvo?");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("wasOrWasNot"));

        columns.add(studentNameColumn);
        columns.add(studentSurnameColumn);
        columns.add(groupNameColumn);
        columns.add(dateColumn);
        columns.add(statusColumn);

        setupTable();
    }
}

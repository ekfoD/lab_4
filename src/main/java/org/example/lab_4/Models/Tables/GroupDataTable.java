package org.example.lab_4.Models.Tables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.lab_4.Models.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupDataTable extends AbstractTable<Group> {
    public GroupDataTable(List<Group> groups) {
        table = new TableView<>();
        columns = new ArrayList<>();
        setupColumns(groups);
    }
    @Override
    public void setupColumns(List<Group> objects) {
        TableColumn<Group, String> nameColumn = new TableColumn<>("Pavadinimas");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Group, Number> studentAmountColumn = new TableColumn<>("Kiek studentų");
        studentAmountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStudents().size()));

        columns.add(nameColumn);
        columns.add(studentAmountColumn);

        setupTable();
    }
}
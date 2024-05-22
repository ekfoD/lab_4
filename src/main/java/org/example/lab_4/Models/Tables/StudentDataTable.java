package org.example.lab_4.Models.Tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.lab_4.Models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDataTable extends AbstractTable<Student> {
    public StudentDataTable(List<Student> students) {
        table = new TableView<>();
        columns = new ArrayList<>();
        setupColumns(students);
    }
    @Override
    public void setupColumns(List<Student> objects) {
        TableColumn<Student, String> nameColumn = new TableColumn<>("Vardas");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> surnameColumn = new TableColumn<>("Pavardė");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Student, String> groupNameColumn = new TableColumn<>("Grupė");
        groupNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGroup().getName()));

        columns.add(nameColumn);
        columns.add(surnameColumn);
        columns.add(groupNameColumn);

        setupTable();
    }
}

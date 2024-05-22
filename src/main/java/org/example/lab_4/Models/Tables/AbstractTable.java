package org.example.lab_4.Models.Tables;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.lab_4.Interfaces.ITable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTable<T> implements ITable<T> {
    protected TableView<T> table;
    protected List<TableColumn> columns;
    @Override
    public void setupTable() {
        if (columns == null)
            return;
        for (TableColumn column : columns) {
            table.getColumns().add(column);
        }
    }
    @Override
    public void updateTable(List<T> objects) {
        table.setItems(FXCollections.observableList(objects));
        table.refresh();        // 2 hours
    }
    @Override
    public abstract void setupColumns(List<T> objects);

    public TableView<T> getTable() {
        return table;
    }
    public void setTable(TableView<T> table) {
        this.table = table;
    }
}

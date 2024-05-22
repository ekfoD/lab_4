package org.example.lab_4.Interfaces;

import java.util.List;

public interface ITable<T> {
    void setupTable();
    void setupColumns(List<T> objects);
    void updateTable(List<T> objects);
}

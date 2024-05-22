package org.example.lab_4.Models;

import java.util.ArrayList;
import java.util.List;

public class SingletonStudentAndGroupList {
    private static SingletonStudentAndGroupList instance;
    private List<Student> studentList = new ArrayList<Student>();
    private List<Group> groupList = new ArrayList<Group>();
    private List<FilteredTableRow> filteredTableRowList = new ArrayList<FilteredTableRow>();

    private SingletonStudentAndGroupList() {}

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<FilteredTableRow> getFilteredTableRowList() {
        return filteredTableRowList;
    }

    public void setFilteredTableRowList(List<FilteredTableRow> filteredTableRowList) {
        this.filteredTableRowList = filteredTableRowList;
    }

    public static SingletonStudentAndGroupList getInstance() {
        if (instance == null) {
            instance = new SingletonStudentAndGroupList();
        }
        return instance;
    }
}

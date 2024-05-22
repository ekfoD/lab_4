package org.example.lab_4.Models;

import java.time.LocalDate;

public class FilteredTableRow {
    public static final String STUDENT_WAS = "Taip";
    public static final String STUDENT_WAS_NOT = "Ne";
    private String studentName;
    private String studentSurname;
    private String groupName;
    private LocalDate date;
    private String wasOrWasNot;

    public FilteredTableRow(String studentName, String studentSurname, String groupName, LocalDate date, Boolean wasOrWasNot) {
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.groupName = groupName;
        this.date = date;
        this.wasOrWasNot = wasOrWasNot ? STUDENT_WAS : STUDENT_WAS_NOT;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWasOrWasNot() {
        return wasOrWasNot;
    }

    public void setWasOrWasNot(String wasOrWasNot) {
        this.wasOrWasNot = wasOrWasNot;
    }
}

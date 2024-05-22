package org.example.lab_4.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String surname;
    private Group group;
    private List<LocalDate> attendanceDates;
    public Student(String name, String surname, Group group) {
        this.name = name;
        this.surname = surname;
        this.group = group;
        attendanceDates = new ArrayList<>();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<LocalDate> getAttendanceDates() {
        return attendanceDates;
    }

    public void setAttendanceDates(List<LocalDate> attendanceDates) {
        this.attendanceDates = attendanceDates;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}

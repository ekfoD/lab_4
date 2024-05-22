package org.example.lab_4.Utilities;

import javafx.scene.control.Alert;
import org.example.lab_4.Models.Group;
import org.example.lab_4.Models.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class InputCheckingUtilities {
    public static boolean doesThisGroupAlreadyExist(String name, List<Group> groupList) {
        for (Group group : groupList) {
            if (Objects.equals(group.getName(), name))
                return true;
        }
        return false;
    }
    public static boolean doesThisStudentAlreadyExist (String name, String surname, List <Student> studentList) {
        for (Student student : studentList) {
            if (Objects.equals(student.getName(), name) && Objects.equals(student.getSurname(), surname))
                return true;
        }
        return false;
    }
    public static boolean wasStudentAlreadyMarked(Student selectedStudent, LocalDate selectedDate) {
        return selectedStudent.getAttendanceDates().contains(selectedDate);
    }

    public static boolean areDatesLegal(LocalDate start, LocalDate end) {
        return end.isAfter(start);
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }
}
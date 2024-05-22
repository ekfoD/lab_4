package org.example.lab_4.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.lab_4.Models.FilteredTableRow;
import org.example.lab_4.Models.Group;
import org.example.lab_4.Models.SingletonStudentAndGroupList;
import org.example.lab_4.Models.Student;
import org.example.lab_4.Models.Tables.FilteredAttendanceTable;
import org.example.lab_4.Task4Application;
import org.example.lab_4.Utilities.ImportExportUtilities;
import org.example.lab_4.Utilities.ListUtilities;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.lab_4.Utilities.InputCheckingUtilities.*;
import static org.example.lab_4.Utilities.ListUtilities.updateStudentList;

public class AttendanceController {

    @FXML
    private DatePicker filterDateEnd;

    @FXML
    private DatePicker filterDateStart;

    @FXML
    private VBox filterTablePlaceHolder;

    @FXML
    private ChoiceBox<Group> groupFilter;

    @FXML
    private ChoiceBox<String> markedDaysBool;

    @FXML
    private DatePicker studentDateForAttendance;

    @FXML
    private ChoiceBox<Student> studentFilter;

    @FXML
    private ChoiceBox<Student> studentSelectionForAttendance;
    private List<Group> groupList;
    private List<Student> studentList;
    private List<FilteredTableRow> filteredTableRowList;
    private FilteredAttendanceTable filteredAttendanceTable;
    private Student emptyStudent;
    private Group emptyGroup;

    @FXML
    void initialize() {
        groupList = SingletonStudentAndGroupList.getInstance().getGroupList();
        studentList = SingletonStudentAndGroupList.getInstance().getStudentList();
        filteredTableRowList = SingletonStudentAndGroupList.getInstance().getFilteredTableRowList();

        filteredAttendanceTable = new FilteredAttendanceTable(filteredTableRowList);
        filteredAttendanceTable.updateTable(filteredTableRowList);
        filterTablePlaceHolder.getChildren().add(filteredAttendanceTable.getTable());

        markedDaysBool.setItems(FXCollections.observableArrayList("Taip", "Ne"));
        updateGraphics();
    }

    @FXML
    void exportData(ActionEvent event) throws IOException {
        ImportExportUtilities.exportAttendanceStatisticsToPDF();
    }
    @FXML   //TODO
    void filterTable(ActionEvent event) {
        if (filterDateStart.getValue() == null || filterDateEnd.getValue() == null
        || markedDaysBool.getValue() == null || (studentFilter.getValue() == null && groupFilter.getValue() == null)) {
            showError("užpildykite reikiamus laukus");
            return;
        } else if (!areDatesLegal(filterDateStart.getValue(), filterDateEnd.getValue())) {
            showError("nelogiškos datos. ar tikrai teisingai suvedėte?");
            return;
        } else if (studentFilter.getValue() != null && groupFilter.getValue() != null) {
            showError("pasirinkite arba grupę arba studentą, ne abu!");
            return;
        }
        LocalDate filterDateStartValue = filterDateStart.getValue();
        LocalDate filterDateEndValue = filterDateEnd.getValue();
        boolean yesOrNo = Objects.equals(markedDaysBool.getValue(), FilteredTableRow.STUDENT_WAS);
        Student selectedStudent = null;
        Group selectedGroup = null;

        if (studentFilter.getValue() != null)
            selectedStudent = studentFilter.getValue();
        if (groupFilter.getValue() != null)
             selectedGroup = groupFilter.getValue();

        filteredTableRowList.clear();

        // galima filtruoti
        if (selectedGroup != null && yesOrNo) {
            for (Student student : selectedGroup.getStudents()) {
                for (LocalDate date : student.getAttendanceDates()) {
                    if ((date.isAfter(filterDateStartValue) || date.equals(filterDateStartValue)) &&
                            (date.isBefore(filterDateEndValue) || date.equals(filterDateEndValue)))
                        filteredTableRowList.add(new FilteredTableRow(student.getName(), student.getSurname(), selectedGroup.getName(), date, true));
                }
            }
        } else if (selectedGroup != null) {
            LocalDate currentDate = filterDateStartValue;

            for (Student student : selectedGroup.getStudents()) {
                while(!currentDate.isAfter(filterDateEndValue)) {
                    if (student.getAttendanceDates().contains(currentDate))
                        filteredTableRowList.add(new FilteredTableRow(student.getName(), student.getSurname(), selectedGroup.getName(), currentDate, true));
                    else
                        filteredTableRowList.add(new FilteredTableRow(student.getName(), student.getSurname(), selectedGroup.getName(), currentDate, false));
                    currentDate = currentDate.plusDays(1);
                }
            }
        } else if (yesOrNo && selectedStudent != null) {
            for (LocalDate date : selectedStudent.getAttendanceDates()) {
                if ((date.isAfter(filterDateStartValue) || date.equals(filterDateStartValue)) &&
                        (date.isBefore(filterDateEndValue) || date.equals(filterDateEndValue)))
                    filteredTableRowList.add(new FilteredTableRow(selectedStudent.getName(), selectedStudent.getSurname(), selectedStudent.getGroup().getName(), date, true));
            }
        } else if (selectedStudent != null) {
            LocalDate currentDate = filterDateStartValue;

            while(!currentDate.isAfter(filterDateEndValue)) {
                if (selectedStudent.getAttendanceDates().contains(currentDate))
                    filteredTableRowList.add(new FilteredTableRow(selectedStudent.getName(), selectedStudent.getSurname(), selectedStudent.getGroup().getName(), currentDate, true));
                else
                    filteredTableRowList.add(new FilteredTableRow(selectedStudent.getName(), selectedStudent.getSurname(), selectedStudent.getGroup().getName(), currentDate, false));
                currentDate = currentDate.plusDays(1);
            }
        }
        updateGraphics();
        SingletonStudentAndGroupList.getInstance().setFilteredTableRowList(filteredTableRowList);
    }
    @FXML
    void goToDataView(ActionEvent event) throws IOException {
        SingletonStudentAndGroupList.getInstance().setGroupList(groupList);
        SingletonStudentAndGroupList.getInstance().setStudentList(studentList);

        // uzdarom input langa
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        // uzloadinam output scena ir settinam student tos scenos to Singleton student
        FXMLLoader fxmlLoader = new FXMLLoader(Task4Application.class.getResource("data-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("duomenys");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void markStudentAttendance(ActionEvent event) {
        Student selectedStudent = studentSelectionForAttendance.getValue();
        LocalDate selectedDate = studentDateForAttendance.getValue();

        if (selectedStudent == null || selectedDate == null) {
            showError("pasirinkite studentą");
            return;
        } else if (wasStudentAlreadyMarked(selectedStudent, selectedDate)) {
            showError("studentas tą dieną jau buvo pažymėtas");
            return;
        }

        selectedStudent.getAttendanceDates().add(selectedDate);
        updateStudentList(selectedStudent, studentList);
        filteredAttendanceTable.getTable().getItems().removeAll(filteredTableRowList);
        updateGraphics();
    }
    void updateGraphics() {
        // table update
        filteredAttendanceTable.updateTable(filteredTableRowList);
        // choice box updates
        studentSelectionForAttendance.getItems().clear();
        studentFilter.getItems().clear();
        groupFilter.getItems().clear();

        for (Group group : groupList)
            groupFilter.getItems().add(group);
        groupFilter.getItems().add(emptyGroup);

        for (Student student : studentList) {
            studentSelectionForAttendance.getItems().add(student);
            studentFilter.getItems().add(student);
        }
        studentFilter.getItems().add(emptyStudent);
    }

}


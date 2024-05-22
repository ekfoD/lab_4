package org.example.lab_4.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.example.lab_4.Models.*;
import org.example.lab_4.Models.Tables.GroupDataTable;
import org.example.lab_4.Models.Tables.StudentDataTable;
import org.example.lab_4.Task4Application;
import org.example.lab_4.Utilities.ImportExportUtilities;
import org.example.lab_4.Utilities.InputCheckingUtilities;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.lab_4.Utilities.ListUtilities.updateGroupList;
import static org.example.lab_4.Utilities.ListUtilities.updateStudentList;

public class DataController {
    @FXML
    private ChoiceBox<Group> assigningStudentGroupChoice;

    @FXML
    private ChoiceBox<Student> assigningStudentStudentChoice;

    @FXML
    private ChoiceBox<Group> creatingStudentGroupChoice;

    @FXML
    private TextField groupNameField;

    @FXML
    private VBox groupTablePlaceHolder;

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField studentSurnameField;

    @FXML
    private VBox studentTablePlaceHolder;
    private List<Group> groupList;
    private List<Student> studentList;
    private StudentDataTable studentDataTable;
    private GroupDataTable groupDataTable;

    @FXML
    void initialize() { // this is called after all FXML files are loaded
        groupList = SingletonStudentAndGroupList.getInstance().getGroupList();
        studentList = SingletonStudentAndGroupList.getInstance().getStudentList();

        studentDataTable = new StudentDataTable(studentList);
        groupDataTable = new GroupDataTable(groupList);

        studentTablePlaceHolder.getChildren().add(studentDataTable.getTable());
        groupTablePlaceHolder.getChildren().add(groupDataTable.getTable());

        studentDataTable.getTable().getColumns().add(getStudentModifyColumn());
        groupDataTable.getTable().getColumns().add(getGroupModifyColumn());

        updateGraphics();
    }
    @FXML
    void assignStudentToGroup(ActionEvent event) {
        if (assigningStudentGroupChoice.getValue() == null || assigningStudentStudentChoice.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "pasirinkite studentą ir grupę");
            alert.show();
            return;
        }

        Student selectedStudent = assigningStudentStudentChoice.getValue();
        Group oldGroup = selectedStudent.getGroup();
        Group selectedGroup = assigningStudentGroupChoice.getValue();

        if (Objects.equals(selectedGroup.getName(), oldGroup.getName())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "šis studentas jau yra šioje grupėje");
            alert.show();
            return;
        }

        selectedStudent.setGroup(selectedGroup);
        selectedGroup.getStudents().add(selectedStudent);
        oldGroup.getStudents().remove(selectedStudent);

        updateGroupList(oldGroup, groupList);
        updateGroupList(selectedGroup, groupList);
        updateStudentList(selectedStudent, studentList);

        updateGraphics();
    }
    @FXML
    void createGroup(ActionEvent event) {
        if (Objects.equals(groupNameField.getText(), "")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "prašau įveskite duomenis");
            alert.show();
            return;
        } else if (InputCheckingUtilities.doesThisGroupAlreadyExist(groupNameField.getText(), groupList)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "tokia grupė jau egzistuoja");
            alert.show();
            return;
        }

        groupList.add(new Group(groupNameField.getText()));
        updateGraphics();
    }
    @FXML
    void createStudent(ActionEvent event) {
        if (Objects.equals(studentNameField.getText(), "") || Objects.equals(studentSurnameField.getText(), "") || creatingStudentGroupChoice.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "prašau įveskite duomenis");
            alert.show();
            return;
        } else if (InputCheckingUtilities.doesThisStudentAlreadyExist(studentNameField.getText(), studentSurnameField.getText(), studentList)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "toks studentas jau egzistuoja");
            alert.show();
            return;
        }

        Group chosenGroup = creatingStudentGroupChoice.getValue();
        Student student = new Student(studentNameField.getText(), studentSurnameField.getText(), chosenGroup);
        studentList.add(student);
        chosenGroup.getStudents().add(student);

        updateGroupList(chosenGroup, groupList);
        updateGraphics();
    }
    @FXML
    void exportData(ActionEvent event) throws IOException {
        ImportExportUtilities.exportStudentsAndGroupsToCSV();
    }
    @FXML
    void goToAttendanceView(ActionEvent event) throws IOException {
        SingletonStudentAndGroupList.getInstance().setGroupList(groupList);
        SingletonStudentAndGroupList.getInstance().setStudentList(studentList);

        // uzdarom input langa
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        // uzloadinam output scena ir settinam student tos scenos to Singleton student
        FXMLLoader fxmlLoader = new FXMLLoader(Task4Application.class.getResource("attendance-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("lankymas");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void importData(ActionEvent event) throws IOException {
        ImportExportUtilities.importStudentsAndGroupsFromCSV();

        groupList = SingletonStudentAndGroupList.getInstance().getGroupList();
        studentList = SingletonStudentAndGroupList.getInstance().getStudentList();

        updateGraphics();
    }
    void updateGraphics() {
        groupDataTable.updateTable(groupList);
        studentDataTable.updateTable(studentList);

        // choice box updates
        creatingStudentGroupChoice.getItems().clear();
        assigningStudentGroupChoice.getItems().clear();
        assigningStudentStudentChoice.getItems().clear();

        for (Group group : groupList) {
            creatingStudentGroupChoice.getItems().add(group);
            assigningStudentGroupChoice.getItems().add(group);
        }

        for (Student student : studentList)
            assigningStudentStudentChoice.getItems().add(student);
    }

    void deleteSelectedGroup(Group selectedGroup) {
        groupList.remove(selectedGroup);

        for (Student student : studentList) {
            if (student.getGroup() == selectedGroup) {
                student.setGroup(new Group("-"));
                break;
            }
        }

        SingletonStudentAndGroupList.getInstance().setGroupList(groupList);
        updateGraphics();
    }
    void editSelectedGroup(Group selectedGroup) {
        TextInputDialog dialog = new TextInputDialog(selectedGroup.getName());
        dialog.setTitle("Redaguoti grupę");
        dialog.setHeaderText(null);
        dialog.setContentText("Įveskite naują grupės pavadinimą:");

        dialog.showAndWait().ifPresent(newName -> {
            if (Objects.equals(newName, "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "prašau įveskite duomenis");
                alert.show();
                return;
            } else if (InputCheckingUtilities.doesThisGroupAlreadyExist(newName, groupList)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "tokia grupė jau egzistuoja");
                alert.show();
                return;
            }
            selectedGroup.setName(newName);
            updateGroupList(selectedGroup, groupList);
            updateGraphics();
        });
    }
    void deleteSelectedStudent(Student selectedStudent) {
        studentList.remove(selectedStudent);

        for (Group group : groupList) {
            group.getStudents().remove(selectedStudent);
        }

        SingletonStudentAndGroupList.getInstance().setStudentList(studentList);
        updateGraphics();
    }
    void editSelectedStudent(Student selectedStudent) {
        // https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Redaguoti studentą");
        dialog.setContentText("Įveskite naujus studento duomenis:");

        ButtonType proceedButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(proceedButtonType, ButtonType.CLOSE);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField studentNameEditField = new TextField();
        studentNameEditField.setPromptText(selectedStudent.getName());
        TextField studentSurnameEditField = new TextField();
        studentSurnameEditField.setPromptText(selectedStudent.getSurname());

        // kaip lentele issidesciusi tai nustatai kas kur
        gridPane.add(new Label("Vardas:"), 0, 0);
        gridPane.add(studentNameEditField, 0, 1);
        gridPane.add(new Label("Pavardė:"), 2, 0);
        gridPane.add(studentSurnameEditField, 2, 1);

        // ta lentele prilygini sitam dialogui
        dialog.getDialogPane().setContent(gridPane);

        // kad kai atidarai is kart fokusas cursor butu ant vardo
        Platform.runLater(studentNameEditField::requestFocus);

        // convertini rezultata tos formos i Pair tipa
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == proceedButtonType) {
                return new Pair<>(studentNameEditField.getText(), studentSurnameEditField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            if (Objects.equals(pair.getKey(), "") || Objects.equals(pair.getValue(), "")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "prašau įveskite duomenis");
                alert.show();
                return;
            } else if (InputCheckingUtilities.doesThisStudentAlreadyExist(pair.getKey(), pair.getValue(), studentList)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "toks studentas jau egzistuoja");
                alert.show();
                return;
            }
            selectedStudent.setName(pair.getKey());
            selectedStudent.setSurname(pair.getValue());
            updateStudentList(selectedStudent, studentList);
            updateGraphics();
        });
    }
    TableColumn<Group, Void> getGroupModifyColumn() {
        TableColumn<Group, Void> editColumn = new TableColumn<>("Veiksmai");
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Keisti");
            private final Button deleteButton = new Button("Trinti");
            private final HBox buttonsContainer = new HBox(editButton, deleteButton);

            {
                // Handle button action (e.g., open a dialog to edit the student)
                editButton.setOnAction(event -> {
                    Group selectedGroup = getTableView().getItems().get(getIndex());
                    editSelectedGroup(selectedGroup);
                });

                // Delete button action
                deleteButton.setOnAction(event -> {
                    Group selectedGroup = getTableView().getItems().get(getIndex());
                    deleteSelectedGroup(selectedGroup);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsContainer);
                }
            }
        });

        return editColumn;
    }
    TableColumn<Student, Void> getStudentModifyColumn() {
        TableColumn<Student, Void> editColumn = new TableColumn<>("Veiksmai");
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Keisti");
            private final Button deleteButton = new Button("Trinti");
            private final HBox buttonsContainer = new HBox(editButton, deleteButton);

            {
                // Handle button action (e.g., open a dialog to edit the student)
                editButton.setOnAction(event -> {
                    Student selectedStudent = getTableView().getItems().get(getIndex());
                    editSelectedStudent(selectedStudent);
                });

                // Delete button action
                deleteButton.setOnAction(event -> {
                    Student selectedStudent = getTableView().getItems().get(getIndex());
                    deleteSelectedStudent(selectedStudent);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsContainer);
                }
            }
        });

        return editColumn;
    }
}

package org.example.lab_4.Utilities;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.example.lab_4.Models.FilteredTableRow;
import org.example.lab_4.Models.Group;
import org.example.lab_4.Models.SingletonStudentAndGroupList;
import org.example.lab_4.Models.Student;
import org.apache.commons.csv.CSVFormat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImportExportUtilities {
    public static void importStudentsAndGroupsFromCSV() throws IOException {
        List<Group> groupList = importGroups("groups.csv");
        List<Student> studentList = importStudents("students.csv", groupList);

        SingletonStudentAndGroupList.getInstance().setStudentList(studentList);
        SingletonStudentAndGroupList.getInstance().setGroupList(groupList);
    }

    public static void exportStudentsAndGroupsToCSV() throws IOException {
        List<Student> studentList = SingletonStudentAndGroupList.getInstance().getStudentList();
        List<Group> groupList = SingletonStudentAndGroupList.getInstance().getGroupList();
//        exportToCSV(studentList, groupList, "export.csv");
        exportGroups(groupList, "groups.csv");
        exportStudents(studentList, "students.csv");
    }

    public static void exportAttendanceStatisticsToPDF() throws IOException {
        List<FilteredTableRow> filteredTableRowList = SingletonStudentAndGroupList.getInstance().getFilteredTableRowList();
        exportToPDF(filteredTableRowList, "filteredTableResults.pdf");
    }

    private static void exportStudents(List<Student> students, String filePath) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT)) {
            for (Student student : students) {  // jei niekada nebuvo tai neeksportuoja
                for (LocalDate date : student.getAttendanceDates()) {
                    printer.printRecord(student.getName(), student.getSurname(), date, student.getGroup().getName());
                }
            }
        }
    }

    private static void exportGroups(List<Group> groups, String filePath) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT)) {
            for (Group group : groups) {
//                for (Student student : group.getStudents()) {
//                    printer.printRecord(group.getName(), student.getName(), student.getSurname());
//                }
                printer.printRecord(group.getName());
            }
        }
    }

    private static List<Student> importStudents(String filePath, List<Group> groupList) throws IOException {
        List<Student> studentList = new ArrayList<>();
        boolean studentAlreadyExists = false;

        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {
                    String name = record.get(0);
                    String surname = record.get(1);
                    LocalDate date = LocalDate.parse(record.get(2)); // Adjust parsing as needed
                    String groupName = record.get(3);

                    for (Group group : groupList) {
                        if (Objects.equals(group.getName(), groupName)) {
                            // sukuri studenta ir jam priskiri jau egzistuojancia grupe (pagal issaugotus duomenis)
                            Student student = new Student(name, surname, group);
                            studentAlreadyExists = false;

                            for (Student tempStudent : studentList) {  // ieskai jau sukurto tokio studento (su tuo paciu vardu/pavarde)
                                if (Objects.equals(tempStudent.getName(), student.getName()) &&
                                        Objects.equals(tempStudent.getSurname(), student.getSurname())) {   // randi? tai data pridedi
                                    studentList.get(studentList.indexOf(tempStudent)).getAttendanceDates().add(date);
                                    studentAlreadyExists = true;
                                    break;
                                }
                            }   // neradai? nu tai ta nauja studenta pridedi

                            if (!studentAlreadyExists) {
                                student.getAttendanceDates().add(date);
                                group.getStudents().add(student);
                                studentList.add(student);
                                break;
                            }
                        }
                    }
            }
        }
        return studentList;
    }

    private static List<Group> importGroups(String filePath) throws IOException {
        List<Group> groupList = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {
                    Group group = new Group(record.get(0));
                groupList.add(group);
            }
        }
        return groupList;
    }

    private static void exportToPDF(List<FilteredTableRow> filteredTableRowList, String filePath) throws IOException {
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer)) {
            Document document = new Document(pdf, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont();
            document.setFont(font);

            document.add(new Paragraph("Studentu sarašas"));

            for (FilteredTableRow filteredTableRow : filteredTableRowList) {
                document.add(new Paragraph("Vardas: " + filteredTableRow.getStudentName() + ", Pavarde: " + filteredTableRow.getStudentSurname() +
                        ", grupe: " + filteredTableRow.getGroupName() + ", data: " + filteredTableRow.getDate().toString() + ", buvo ar ne: " + filteredTableRow.getWasOrWasNot()));
            }
        }
    }
}

package org.example.lab_4.Utilities;

import org.example.lab_4.Models.Group;
import org.example.lab_4.Models.SingletonStudentAndGroupList;
import org.example.lab_4.Models.Student;

import java.util.List;
import java.util.Objects;

public class ListUtilities {
    public static void updateGroupList(Group selectedGroup, List<Group> groupList) {
        for (Group group : groupList) {
            if (Objects.equals(group.getName(), selectedGroup.getName())) {
                groupList.set(groupList.indexOf(group), selectedGroup);
                SingletonStudentAndGroupList.getInstance().setGroupList(groupList);
                break;
            }
        }
    }
    public static void updateStudentList(Student selectedStudent, List<Student> studentList) {
        for (Student student : studentList) {
            if (Objects.equals(student.getName(), selectedStudent.getName()) && Objects.equals(student.getSurname(), selectedStudent.getSurname())) {
                studentList.set(studentList.indexOf(student), selectedStudent);
                SingletonStudentAndGroupList.getInstance().setStudentList(studentList);
                break;
            }
        }
    }
}

package ua.foxminded.school.Services;

import ua.foxminded.school.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> findStudentsByCourseName(String courseName);
    void addStudent(Student student);
    void deleteStudent(int studentId);
    void addStudentToCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);

}

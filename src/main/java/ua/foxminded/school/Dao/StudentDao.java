package ua.foxminded.school.Dao;


import ua.foxminded.school.model.Student;

import java.util.*;


public interface StudentDao {
    void save(Student student);
    Optional<Student> findById(int id);
    List<Student> findAll();
    void update(Student student);
    void deleteById(int id);

    List<Student> findByCourseName(String courseName);
    void assignToGroup(int studentId, int groupId);
    void addToCourse(int studentId, int courseId);
    void removeFromCourse(int studentId, int courseId);
}

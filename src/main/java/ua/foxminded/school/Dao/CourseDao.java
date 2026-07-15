package ua.foxminded.school.Dao;

import ua.foxminded.school.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {

    void save(Course course);

    Optional<Course> findById(int id);

    Optional<Course> findByName(String name);
    List<Course> findAll();
    List<Course> findByStudentId(int studentId);
    void update(Course course);
    void deleteById(int id);
}



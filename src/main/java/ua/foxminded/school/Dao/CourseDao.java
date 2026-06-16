package ua.foxminded.school.Dao;

import ua.foxminded.school.db.ConnectionManager;
import ua.foxminded.school.model.Course;

import java.util.Optional;

public interface CourseDao {

    void save(Course course);

    Optional<Course> findById(int id);

    Optional<Course> findByName(String name);
}



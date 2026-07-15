package ua.foxminded.school.Services;

import ua.foxminded.school.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAllCourses();
    List<Course> findCoursesByStudentId(int studentId);
}
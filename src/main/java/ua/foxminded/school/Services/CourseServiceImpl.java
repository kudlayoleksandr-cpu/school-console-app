package ua.foxminded.school.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.foxminded.school.model.Course;
import ua.foxminded.school.repository.CourseRepository;


import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseDao;

    public CourseServiceImpl(CourseRepository courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public List<Course> findAllCourses() {
        log.info("Retrieving all courses");
        return courseDao.findAll();
    }

    @Override
    public List<Course> findCoursesByStudentId(int studentId) {
        log.info("Retrieving courses for student {}", studentId);
        return courseDao.findByStudentId(studentId);
    }
}
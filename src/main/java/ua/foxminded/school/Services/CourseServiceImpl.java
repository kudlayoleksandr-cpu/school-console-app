package ua.foxminded.school.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.foxminded.school.Dao.CourseDao;
import ua.foxminded.school.model.Course;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
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
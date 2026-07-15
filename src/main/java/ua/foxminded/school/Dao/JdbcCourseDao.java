package ua.foxminded.school.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.school.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCourseDao implements CourseDao {

    private static final Logger log = LoggerFactory.getLogger(JdbcCourseDao.class);

    private static final String INSERT =
            "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";
    private static final String FIND_BY_ID =
            "SELECT * FROM courses WHERE course_id = ?";
    private static final String FIND_BY_NAME =
            "SELECT * FROM courses WHERE course_name = ?";
    private static final String FIND_ALL = "SELECT * FROM courses";
    private static final String FIND_BY_STUDENT_ID =
            "SELECT c.* FROM courses c "
                    + "JOIN students_courses sc ON c.course_id = sc.course_id "
                    + "WHERE sc.student_id = ?";
    private static final String UPDATE =
            "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String DELETE = "DELETE FROM courses WHERE course_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();

    public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Course course) {
        log.debug("Saving course '{}'", course.getCourseName());
        jdbcTemplate.update(INSERT, course.getCourseName(), course.getCourseDescription());
    }

    @Override
    public Optional<Course> findById(int id) {
        log.debug("Looking up course by id {}", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, courseRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            log.debug("No course found with id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Course> findByName(String name) {
        log.debug("Looking up course by name '{}'", name);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME, courseRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            log.debug("No course found with name '{}'", name);
            return Optional.empty();
        }
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = jdbcTemplate.query(FIND_ALL, courseRowMapper);
        log.debug("Found {} courses", courses.size());
        return courses;
    }

    @Override
    public List<Course> findByStudentId(int studentId) {
        List<Course> courses = jdbcTemplate.query(FIND_BY_STUDENT_ID, courseRowMapper, studentId);
        log.debug("Found {} courses for student {}", courses.size(), studentId);
        return courses;
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting course with id {}", id);
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public void update(Course course) {
        log.debug("Updating course with id {}", course.getCourseId());
        jdbcTemplate.update(UPDATE,
                course.getCourseName(), course.getCourseDescription(), course.getCourseId());
    }
}


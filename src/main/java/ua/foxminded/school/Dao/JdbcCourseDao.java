package ua.foxminded.school.Dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.school.model.Course;

import java.util.Optional;

@Repository
public class JdbcCourseDao implements CourseDao {

    private static final String INSERT =
            "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";
    private static final String FIND_BY_ID =
            "SELECT * FROM courses WHERE course_id = ?";
    private static final String FIND_BY_NAME =
            "SELECT * FROM courses WHERE course_name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();

    public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Course course) {
        jdbcTemplate.update(INSERT, course.getCourseName(), course.getCourseDescription());
    }

    @Override
    public Optional<Course> findById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, courseRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Course> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME, courseRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}


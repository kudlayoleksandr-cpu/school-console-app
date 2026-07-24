package ua.foxminded.school.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.school.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStudentDao implements StudentDao {

    private static final Logger log = LoggerFactory.getLogger(JdbcStudentDao.class);

    private static final String INSERT =
            "INSERT INTO students(group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM students WHERE student_id = ?";
    private static final String FIND_ALL = "SELECT * FROM students";
    private static final String UPDATE =
            "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE = "DELETE FROM students WHERE student_id = ?";
    private static final String FIND_BY_COURSE_NAME =
            "SELECT s.* FROM students s "
                    + "JOIN students_courses sc ON s.student_id = sc.student_id "
                    + "JOIN courses c ON sc.course_id = c.course_id "
                    + "WHERE c.course_name = ?";
    private static final String ASSIGN_TO_GROUP =
            "UPDATE students SET group_id = ? WHERE student_id = ?";
    private static final String ADD_TO_COURSE =
            "INSERT INTO students_courses(student_id, course_id) VALUES (?, ?)";
    private static final String REMOVE_FROM_COURSE =
            "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> studentRowMapper = new StudentRowMapper();

    public JdbcStudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Student student) {
        log.debug("Saving student {} {} in group {}",
                student.getfirstName(), student.getsecondName(), student.getGroupId());
        jdbcTemplate.update(INSERT,
                student.getGroupId(), student.getfirstName(), student.getsecondName());
    }

    @Override
    public Optional<Student> findById(int id) {
        log.debug("Looking up student by id {}", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, studentRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No student found with id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = jdbcTemplate.query(FIND_ALL, studentRowMapper);
        log.debug("Found {} students", students.size());
        return students;
    }

    @Override
    public void update(Student student) {
        log.debug("Updating student with id {}", student.getStudentId());
        jdbcTemplate.update(UPDATE,
                student.getGroupId(), student.getfirstName(), student.getsecondName(),
                student.getStudentId());
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting student with id {}", id);
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public List<Student> findByCourseName(String courseName) {
        List<Student> students = jdbcTemplate.query(FIND_BY_COURSE_NAME, studentRowMapper, courseName);
        log.debug("Found {} students on course '{}'", students.size(), courseName);
        return students;
    }

    @Override
    public void assignToGroup(int studentId, int groupId) {
        log.debug("Assigning student {} to group {}", studentId, groupId);
        jdbcTemplate.update(ASSIGN_TO_GROUP, groupId, studentId);
    }

    @Override
    public void addToCourse(int studentId, int courseId) {
        log.debug("Adding student {} to course {}", studentId, courseId);
        jdbcTemplate.update(ADD_TO_COURSE, studentId, courseId);
    }

    @Override
    public void removeFromCourse(int studentId, int courseId) {
        log.debug("Removing student {} from course {}", studentId, courseId);
        jdbcTemplate.update(REMOVE_FROM_COURSE, studentId, courseId);
    }
}


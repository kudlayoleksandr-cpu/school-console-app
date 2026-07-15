package ua.foxminded.school.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.foxminded.school.Dao.StudentDao;
import ua.foxminded.school.model.Student;
import java.util.List;


@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        log.info("Finding students enrolled on course '{}'", courseName);
        List<Student> students = studentDao.findByCourseName(courseName);
        log.debug("Found {} students on course '{}'", students.size(), courseName);
        return students;
    }

    @Override
    public void addStudent(Student student) {
        log.info("Adding student {} {}", student.getfirstName(), student.getsecondName());
        studentDao.save(student);
    }

    @Override
    public void deleteStudent(int studentId) {
        log.info("Deleting student with id {}", studentId);
        studentDao.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Cannot delete student {}: not found", studentId);
                    return new IllegalArgumentException("No student with id " + studentId);
                });
        studentDao.deleteById(studentId);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        log.info("Adding student {} to course {}", studentId, courseId);
        studentDao.addToCourse(studentId, courseId);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        log.info("Removing student {} from course {}", studentId, courseId);
        studentDao.removeFromCourse(studentId, courseId);
    }
}
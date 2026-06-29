package ua.foxminded.school.Services;
import org.springframework.stereotype.Service;
import ua.foxminded.school.Dao.StudentDao;
import ua.foxminded.school.model.Student;
import java.util.List;


@Service
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        return studentDao.findByCourseName(courseName);
    }

    @Override
    public void addStudent(Student student) {
        studentDao.save(student);
    }

    @Override
    public void deleteStudent(int studentId) {
        studentDao.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("No student with id " + studentId));
        studentDao.deleteById(studentId);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        studentDao.addToCourse(studentId, courseId);
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        studentDao.removeFromCourse(studentId, courseId);
    }
}
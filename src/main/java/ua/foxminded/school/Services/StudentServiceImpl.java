package ua.foxminded.school.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.school.model.Course;
import ua.foxminded.school.repository.CourseRepository;
import ua.foxminded.school.repository.StudentRepository;
import ua.foxminded.school.model.Student;

import java.util.List;


@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Student> findStudentsByCourseName(String courseName) {
        log.info("Finding students enrolled on course '{}'", courseName);
        List<Student> students = studentRepository.findByCourseName(courseName);
        log.debug("Found {} students on course '{}'", students.size(), courseName);
        return students;
    }

    @Override
    public void addStudent(Student student) {
        log.info("Adding student {} {}", student.getfirstName(), student.getsecondName());
        studentRepository.save(student);
    }

    @Override
    public void deleteStudent(int studentId) {
        log.info("Deleting student with id {}", studentId);
        studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Cannot delete student {}: not found", studentId);
                    return new IllegalArgumentException("No student with id " + studentId);
                });
        studentRepository.deleteById(studentId);
    }

    @Override
    @Transactional
    public void addStudentToCourse(int studentId, int courseId) {
        log.info("Adding student {} to course {}", studentId, courseId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Cannot enroll: student {} does not exist", studentId);
                    return new IllegalArgumentException("No student with id " + studentId);
                });
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Cannot enroll: course {} does not exist", courseId);
                    return new IllegalArgumentException("No course with id " + courseId);
                });
        boolean alreadyEnrolled = courseRepository.findByStudentId(studentId).stream()
                .anyMatch(c -> c.getCourseId() == courseId);
        if (alreadyEnrolled) {
            log.warn("Student {} is already enrolled on course {}", studentId, courseId);
            throw new IllegalArgumentException(
                    "Student " + studentId + " is already enrolled on course " + courseId);
        }
        student.enroll(course);
    }

    @Transactional
    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        log.info("Removing student {} from course {}", studentId, courseId);
        boolean enrolled = courseRepository.findByStudentId(studentId).stream()
                .anyMatch(c -> c.getCourseId() == courseId);
        if (!enrolled) {
            log.warn("Student {} is not enrolled on course {}", studentId, courseId);
            throw new IllegalArgumentException(
                    "Student " + studentId + " is not enrolled on course " + courseId);
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.warn("Cannot remove: student {} does not exist", studentId);
                    return new IllegalArgumentException("No student with id " + studentId);
                });
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Cannot remove: course {} does not exist", courseId);
                    return new IllegalArgumentException("No course with id " + courseId);
                });

        student.withdraw(course);
    }
}
package ua.foxminded.school.Dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.school.Dao.StudentDao;
import ua.foxminded.school.model.Course;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class JpaStudentDao implements StudentDao {

    private static final Logger log = LoggerFactory.getLogger(JpaStudentDao.class);

    private static final String FIND_ALL = "select s from Student s";
    private static final String FIND_BY_COURSE_NAME =
            "select s from Student s join s.courses c where c.courseName = :courseName";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Student student) {
        log.debug("Saving student {} {}", student.getfirstName(), student.getsecondName());
        em.persist(student);
    }

    @Override
    public Optional<Student> findById(int id) {
        log.debug("Looking up student by id {}", id);
        Optional<Student> student = Optional.ofNullable(em.find(Student.class, id));
        if (student.isEmpty()) {
            log.warn("No student found with id {}", id);
        }
        return student;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = em.createQuery(FIND_ALL, Student.class).getResultList();
        log.debug("Found {} students", students.size());
        return students;
    }

    @Override
    @Transactional
    public void update(Student student) {
        log.debug("Updating student with id {}", student.getStudentId());
        em.merge(student);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        log.debug("Deleting student with id {}", id);
        Student student = em.find(Student.class, id);
        if (student == null) {
            log.warn("No student found with id {}, nothing to delete", id);
            return;
        }
        em.remove(student);
    }

    @Override
    public List<Student> findByCourseName(String courseName) {
        List<Student> students = em.createQuery(FIND_BY_COURSE_NAME, Student.class)
                .setParameter("courseName", courseName)
                .getResultList();
        log.debug("Found {} students on course '{}'", students.size(), courseName);
        return students;
    }

    @Override
    @Transactional
    public void assignToGroup(int studentId, int groupId) {
        log.debug("Assigning student {} to group {}", studentId, groupId);
        Student student = em.find(Student.class, studentId);
        if (student == null) {
            log.warn("No student found with id {}, nothing to assign", studentId);
            return;
        }
        student.setGroup(em.getReference(Group.class, groupId));
    }

    @Override
    @Transactional
    public void addToCourse(int studentId, int courseId) {
        log.debug("Adding student {} to course {}", studentId, courseId);
        Student student = em.find(Student.class, studentId);
        Course course = em.find(Course.class, courseId);
        if (student == null || course == null) {
            log.warn("Cannot enroll student {} on course {}: one of them does not exist",
                    studentId, courseId);
            return;
        }
        student.enroll(course);
    }

    @Override
    @Transactional
    public void removeFromCourse(int studentId, int courseId) {
        log.debug("Removing student {} from course {}", studentId, courseId);
        Student student = em.find(Student.class, studentId);
        Course course = em.find(Course.class, courseId);
        if (student == null || course == null) {
            log.warn("Cannot remove student {} from course {}: one of them does not exist",
                    studentId, courseId);
            return;
        }
        student.withdraw(course);
    }
}

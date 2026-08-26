package ua.foxminded.school.Dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.school.Dao.CourseDao;
import ua.foxminded.school.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class JpaCourseDao implements CourseDao {

    private static final Logger log = LoggerFactory.getLogger(JpaCourseDao.class);

    private static final String FIND_ALL = "select c from Course c";
    private static final String FIND_BY_NAME =
            "select c from Course c where c.courseName = :courseName";
    private static final String FIND_BY_STUDENT_ID =
            "select c from Course c join c.students s where s.studentId = :studentId";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Course course) {
        log.debug("Saving course '{}'", course.getCourseName());
        em.persist(course);
    }

    @Override
    public Optional<Course> findById(int id) {
        log.debug("Looking up course by id {}", id);
        Optional<Course> course = Optional.ofNullable(em.find(Course.class, id));
        if (course.isEmpty()) {
            log.warn("No course found with id {}", id);
        }
        return course;
    }

    @Override
    public Optional<Course> findByName(String name) {
        log.debug("Looking up course by name '{}'", name);
        Optional<Course> course = em.createQuery(FIND_BY_NAME, Course.class)
                .setParameter("courseName", name)
                .getResultStream()
                .findFirst();
        if (course.isEmpty()) {
            log.warn("No course found with name '{}'", name);
        }
        return course;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = em.createQuery(FIND_ALL, Course.class).getResultList();
        log.debug("Found {} courses", courses.size());
        return courses;
    }

    @Override
    public List<Course> findByStudentId(int studentId) {
        List<Course> courses = em.createQuery(FIND_BY_STUDENT_ID, Course.class)
                .setParameter("studentId", studentId)
                .getResultList();
        log.debug("Found {} courses for student {}", courses.size(), studentId);
        return courses;
    }

    @Override
    @Transactional
    public void update(Course course) {
        log.debug("Updating course with id {}", course.getCourseId());
        em.merge(course);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        log.debug("Deleting course with id {}", id);
        Course course = em.find(Course.class, id);
        if (course == null) {
            log.warn("No course found with id {}, nothing to delete", id);
            return;
        }
        em.remove(course);
    }
}

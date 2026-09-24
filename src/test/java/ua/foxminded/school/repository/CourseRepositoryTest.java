package ua.foxminded.school.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.school.model.Course;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository dao;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByIdShouldReturnCourseWhenPresent() {
        assertThat(dao.findById(1)).get()
                .extracting(Course::getCourseName, Course::getCourseDescription)
                .containsExactly("Math", "Numbers and equations");
    }

    @Test
    void findByIdShouldReturnEmptyWhenMissing() {
        assertThat(dao.findById(999)).isEmpty();
    }

    @Test
    void findByNameShouldReturnCourseWhenPresent() {
        assertThat(dao.findByCourseName("Biology")).get()
                .extracting(Course::getCourseId)
                .isEqualTo(2);
    }

    @Test
    void findByNameShouldReturnEmptyWhenMissing() {
        assertThat(dao.findByCourseName("Astrology")).isEmpty();
    }

    @Test
    void findAllShouldReturnEveryCourse() {
        assertThat(dao.findAll())
                .extracting(Course::getCourseName)
                .containsExactlyInAnyOrder("Math", "Biology", "Physics");
    }

    @Test
    void findByStudentIdShouldReturnCoursesOfThatStudent() {
        assertThat(dao.findByStudentId(1))
                .extracting(Course::getCourseName)
                .containsExactlyInAnyOrder("Math", "Biology");
    }

    @Test
    void findByStudentIdShouldReturnEmptyListForStudentWithoutCourses() {
        assertThat(dao.findByStudentId(4)).isEmpty();
    }

    @Test
    void saveShouldPersistCourseAndAssignId() {
        Course course = new Course("Chemistry", "Reactions");

        dao.save(course);
        flushAndClear();

        assertThat(course.getCourseId()).isNotNull();
        assertThat(em.find(Course.class, course.getCourseId()).getCourseName()).isEqualTo("Chemistry");
    }

    @Test
    void updateShouldChangeCourseFields() {
        dao.save(new Course(1, "Advanced Math", "Harder numbers"));
        flushAndClear();

        Course reloaded = em.find(Course.class, 1);
        assertThat(reloaded.getCourseName()).isEqualTo("Advanced Math");
        assertThat(reloaded.getCourseDescription()).isEqualTo("Harder numbers");
    }

    @Test
    void deleteByIdShouldRemoveCourseWithItsEnrollments() {
        dao.deleteById(3);
        flushAndClear();

        assertThat(em.find(Course.class, 3)).isNull();
        assertThat(dao.findByStudentId(3)).isEmpty();
    }

    @Test
    void deleteByIdShouldDoNothingWhenCourseMissing() {
        dao.deleteById(999);
        flushAndClear();

        assertThat(dao.findAll()).hasSize(3);
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}

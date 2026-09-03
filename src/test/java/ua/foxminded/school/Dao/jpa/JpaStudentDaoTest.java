package ua.foxminded.school.Dao.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.school.Dao.StudentDao;
import ua.foxminded.school.model.Course;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        JpaStudentDao.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class JpaStudentDaoTest {

    @Autowired
    private StudentDao dao;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByIdShouldReturnStudentWhenPresent() {
        Optional<Student> found = dao.findById(1);

        assertThat(found).isPresent();
        assertThat(found.get().getfirstName()).isEqualTo("John");
        assertThat(found.get().getsecondName()).isEqualTo("Doe");
        assertThat(found.get().getGroup().getGroupName()).isEqualTo("AA-11");
    }

    @Test
    void findByIdShouldReturnEmptyWhenMissing() {
        assertThat(dao.findById(999)).isEmpty();
    }

    @Test
    void findAllShouldReturnEveryStudent() {
        List<Student> students = dao.findAll();

        assertThat(students)
                .extracting(Student::getfirstName)
                .containsExactlyInAnyOrder("John", "Jane", "Max", "Lone");
    }

    @Test
    void saveShouldPersistStudentAndAssignId() {
        Group group = em.find(Group.class, 1);
        Student student = new Student(group, "New", "Comer");

        dao.save(student);
        flushAndClear();

        assertThat(student.getStudentId()).isNotNull();
        Student reloaded = em.find(Student.class, student.getStudentId());
        assertThat(reloaded.getfirstName()).isEqualTo("New");
        assertThat(reloaded.getsecondName()).isEqualTo("Comer");
        assertThat(reloaded.getGroup().getGroupId()).isEqualTo(1);
    }

    @Test
    void updateShouldChangeStudentFields() {
        Student detached = new Student(1, em.find(Group.class, 2), "Johnny", "Dodo");

        dao.update(detached);
        flushAndClear();

        Student reloaded = em.find(Student.class, 1);
        assertThat(reloaded.getfirstName()).isEqualTo("Johnny");
        assertThat(reloaded.getsecondName()).isEqualTo("Dodo");
        assertThat(reloaded.getGroup().getGroupId()).isEqualTo(2);
    }

    @Test
    void deleteByIdShouldRemoveStudentWithHisEnrollments() {
        dao.deleteById(1);
        flushAndClear();

        assertThat(em.find(Student.class, 1)).isNull();
        assertThat(countEnrollmentsOf(1)).isZero();
        assertThat(dao.findAll()).hasSize(3);
    }

    @Test
    void deleteByIdShouldDoNothingWhenStudentMissing() {
        dao.deleteById(999);
        flushAndClear();

        assertThat(dao.findAll()).hasSize(4);
    }

    @Test
    void findByCourseNameShouldReturnEnrolledStudentsOnly() {
        List<Student> students = dao.findByCourseName("Math");

        assertThat(students)
                .extracting(Student::getStudentId)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void findByCourseNameShouldReturnEmptyListForUnknownCourse() {
        assertThat(dao.findByCourseName("Astrology")).isEmpty();
    }

    @Test
    void assignToGroupShouldSetGroupOfStudentWithoutOne() {
        dao.assignToGroup(4, 3);
        flushAndClear();

        assertThat(em.find(Student.class, 4).getGroup().getGroupName()).isEqualTo("CC-33");
    }

    @Test
    void assignToGroupShouldDoNothingWhenStudentMissing() {
        dao.assignToGroup(999, 1);
        flushAndClear();

        assertThat(dao.findById(999)).isEmpty();
    }

    @Test
    void addToCourseShouldCreateEnrollment() {
        dao.addToCourse(4, 1);
        flushAndClear();

        assertThat(em.find(Student.class, 4).getCourses())
                .extracting(Course::getCourseName)
                .containsExactly("Math");
    }

    @Test
    void addToCourseShouldDoNothingWhenCourseMissing() {
        dao.addToCourse(4, 999);
        flushAndClear();

        assertThat(countEnrollmentsOf(4)).isZero();
    }

    @Test
    void removeFromCourseShouldDropOnlyThatEnrollment() {
        dao.removeFromCourse(1, 1);
        flushAndClear();

        assertThat(em.find(Student.class, 1).getCourses())
                .extracting(Course::getCourseName)
                .containsExactly("Biology");
    }

    @Test
    void removeFromCourseShouldDoNothingWhenStudentMissing() {
        dao.removeFromCourse(999, 1);
        flushAndClear();

        assertThat(dao.findByCourseName("Math")).hasSize(2);
    }

    /**
     * The DAO shares the test's transaction and persistence context, so pending changes have to be
     * pushed to the database and the context emptied before assertions really re-read the rows.
     */
    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    private long countEnrollmentsOf(int studentId) {
        return em.getEntityManager()
                .createQuery("select count(c) from Student s join s.courses c where s.studentId = :id", Long.class)
                .setParameter("id", studentId)
                .getSingleResult();
    }
}

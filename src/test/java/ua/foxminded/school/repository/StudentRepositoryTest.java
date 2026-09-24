package ua.foxminded.school.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository dao;

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

        dao.save(detached);
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
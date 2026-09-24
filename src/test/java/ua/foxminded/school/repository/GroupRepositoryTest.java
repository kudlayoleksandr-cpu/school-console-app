package ua.foxminded.school.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.school.model.Group;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GroupRepositoryTest {

    @Autowired
    private GroupRepository dao;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByIdShouldReturnGroupWhenPresent() {
        assertThat(dao.findById(1)).get()
                .extracting(Group::getGroupName)
                .isEqualTo("AA-11");
    }

    @Test
    void findByIdShouldReturnEmptyWhenMissing() {
        assertThat(dao.findById(999)).isEmpty();
    }

    @Test
    void findAllShouldReturnEveryGroup() {
        assertThat(dao.findAll())
                .extracting(Group::getGroupName)
                .containsExactlyInAnyOrder("AA-11", "BB-22", "CC-33");
    }

    @Test
    void saveShouldPersistGroupAndAssignId() {
        Group group = new Group("DD-44");

        dao.save(group);
        flushAndClear();

        assertThat(group.getGroupId()).isNotNull();
        assertThat(em.find(Group.class, group.getGroupId()).getGroupName()).isEqualTo("DD-44");
    }

    @Test
    void updateShouldRenameGroup() {
        dao.save(new Group(1, "ZZ-99"));
        flushAndClear();

        assertThat(em.find(Group.class, 1).getGroupName()).isEqualTo("ZZ-99");
    }

    @Test
    void deleteByIdShouldRemoveEmptyGroup() {
        dao.deleteById(3);
        flushAndClear();

        assertThat(em.find(Group.class, 3)).isNull();
        assertThat(dao.findAll()).hasSize(2);
    }

    @Test
    void deleteByIdShouldDoNothingWhenGroupMissing() {
        dao.deleteById(999);
        flushAndClear();

        assertThat(dao.findAll()).hasSize(3);
    }

    @Test
    void findGroupsByStudentCountShouldReturnGroupsAtOrBelowLimit() {
        // AA-11 has 2 students, BB-22 has 1, CC-33 has none
        assertThat(dao.findGroupsByStudentCount(1))
                .extracting(Group::getGroupName)
                .containsExactlyInAnyOrder("BB-22", "CC-33");
    }

    @Test
    void findGroupsByStudentCountShouldReturnOnlyEmptyGroupsForZero() {
        assertThat(dao.findGroupsByStudentCount(0))
                .extracting(Group::getGroupName)
                .containsExactly("CC-33");
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}

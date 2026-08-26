package ua.foxminded.school.Dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.school.Dao.GroupDao;
import ua.foxminded.school.model.Group;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class JpaGroupDao implements GroupDao {

    private static final Logger log = LoggerFactory.getLogger(JpaGroupDao.class);

    private static final String FIND_ALL = "select g from StudyGroup g";
    private static final String FIND_BY_STUDENT_COUNT =
            "select g from StudyGroup g where size(g.students) <= :maxCount";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Group group) {
        log.debug("Saving group '{}'", group.getGroupName());
        em.persist(group);
    }

    @Override
    public Optional<Group> findById(int id) {
        log.debug("Looking up group by id {}", id);
        Optional<Group> group = Optional.ofNullable(em.find(Group.class, id));
        if (group.isEmpty()) {
            log.warn("No group found with id {}", id);
        }
        return group;
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = em.createQuery(FIND_ALL, Group.class).getResultList();
        log.debug("Found {} groups", groups.size());
        return groups;
    }

    @Override
    @Transactional
    public void update(Group group) {
        log.debug("Updating group with id {}", group.getGroupId());
        em.merge(group);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        log.debug("Deleting group with id {}", id);
        Group group = em.find(Group.class, id);
        if (group == null) {
            log.warn("No group found with id {}, nothing to delete", id);
            return;
        }
        em.remove(group);
    }

    @Override
    public List<Group> findGroupsByStudentCount(int maxCount) {
        List<Group> groups = em.createQuery(FIND_BY_STUDENT_COUNT, Group.class)
                .setParameter("maxCount", maxCount)
                .getResultList();
        log.debug("Found {} groups with <= {} students", groups.size(), maxCount);
        return groups;
    }
}

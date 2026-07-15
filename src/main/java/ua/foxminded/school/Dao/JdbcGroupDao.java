package ua.foxminded.school.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.school.model.Group;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGroupDao implements GroupDao {

    private static final Logger log = LoggerFactory.getLogger(JdbcGroupDao.class);

    private static final String INSERT = "INSERT INTO groups(group_name) VALUES (?)";
    private static final String FIND_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
    private static final String FIND_ALL = "SELECT * FROM groups";
    private static final String UPDATE = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE = "DELETE FROM groups WHERE group_id = ?";
    private static final String FIND_BY_STUDENT_COUNT =
            "SELECT g.group_id, g.group_name "
                    + "FROM groups g "
                    + "LEFT JOIN students s ON g.group_id = s.group_id "
                    + "GROUP BY g.group_id, g.group_name "
                    + "HAVING COUNT(s.student_id) <= ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Group> groupRowMapper = new GroupRowMapper();

    public JdbcGroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Group group) {
        log.debug("Saving group '{}'", group.getGroupName());
        jdbcTemplate.update(INSERT, group.getGroupName());
    }

    @Override
    public Optional<Group> findById(int id) {
        log.debug("Looking up group by id {}", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, groupRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            log.debug("No group found with id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = jdbcTemplate.query(FIND_ALL, groupRowMapper);
        log.debug("Found {} groups", groups.size());
        return groups;
    }

    @Override
    public void update(Group group) {
        log.debug("Updating group with id {}", group.getGroupId());
        jdbcTemplate.update(UPDATE, group.getGroupName(), group.getGroupId());
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting group with id {}", id);
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public List<Group> findGroupsByStudentCount(int maxCount) {
        List<Group> groups = jdbcTemplate.query(FIND_BY_STUDENT_COUNT, groupRowMapper, maxCount);
        log.debug("Found {} groups with <= {} students", groups.size(), maxCount);
        return groups;
    }
}

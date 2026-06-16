package ua.foxminded.school.Dao;
import ua.foxminded.school.model.Group;
import java.util.*;

public interface GroupDao {
    void save(Group group);
    Optional<Group> findById(int id);
    List<Group> findAll();
    void update(Group group);
    void deleteById(int id);
    List<Group> findGroupsByStudentCount(int maxCount);
}

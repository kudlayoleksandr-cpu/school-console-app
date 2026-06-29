package ua.foxminded.school.Services;

import org.springframework.stereotype.Service;
import ua.foxminded.school.Dao.GroupDao;
import ua.foxminded.school.model.Group;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupDao groupDao;
    public GroupServiceImpl(GroupDao groupDao){
        this.groupDao=groupDao;
    }
    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxCount){
        return groupDao.findGroupsByStudentCount(maxCount);
    }

}

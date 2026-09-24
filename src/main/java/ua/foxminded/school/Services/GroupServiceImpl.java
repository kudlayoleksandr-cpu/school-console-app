package ua.foxminded.school.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.repository.GroupRepository;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    public GroupServiceImpl(GroupRepository groupRepository){
        this.groupRepository =groupRepository;
    }
    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int maxCount){
        log.info("Finding groups with less or equal {} students", maxCount);
        List<Group> groups = groupRepository.findGroupsByStudentCount(maxCount);
        log.debug("Found {} groups with <= {} students", groups.size(), maxCount);
        return groups;
    }

}

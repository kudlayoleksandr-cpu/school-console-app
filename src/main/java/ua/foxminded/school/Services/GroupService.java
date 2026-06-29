package ua.foxminded.school.Services;

import ua.foxminded.school.model.Group;

import java.util.List;

public interface GroupService {
    List<Group> findGroupsWithLessOrEqualStudents(int maxCount);
}


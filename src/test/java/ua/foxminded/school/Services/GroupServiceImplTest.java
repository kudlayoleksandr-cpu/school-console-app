package ua.foxminded.school.Services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.repository.GroupRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {

    @MockitoBean
    GroupRepository groupRepository;

    @Autowired
    GroupServiceImpl groupService;

    @Test
    void shouldReturnGroupsWithLessOrEqualStudents() {
        List<Group> expected = List.of(new Group(1, "AA-11"), new Group(2, "BB-22"));
        when(groupRepository.findGroupsByStudentCount(15)).thenReturn(expected);

        List<Group> result = groupService.findGroupsWithLessOrEqualStudents(15);

        assertEquals(expected, result);
        verify(groupRepository).findGroupsByStudentCount(15);
    }

    @Test
    void shouldReturnEmptyListWhenNoGroupsMatch() {
        when(groupRepository.findGroupsByStudentCount(0)).thenReturn(List.of());

        List<Group> result = groupService.findGroupsWithLessOrEqualStudents(0);

        assertEquals(List.of(), result);
        verify(groupRepository).findGroupsByStudentCount(0);
    }
}

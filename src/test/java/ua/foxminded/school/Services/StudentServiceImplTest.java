package ua.foxminded.school.Services;

import ua.foxminded.school.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.foxminded.school.repository.CourseRepository;
import ua.foxminded.school.model.Course;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {

    @MockitoBean
    StudentRepository studentDao;

    @MockitoBean
    CourseRepository courseDao;

    @Autowired
    StudentServiceImpl studentService;

    @Test
    void shouldFindStudentsByCourseName() {
        List<Student> expected = List.of(new Student(1, new Group(1, "AA-11"), "John", "Doe"));
        when(studentDao.findByCourseName("Math")).thenReturn(expected);

        List<Student> result = studentService.findStudentsByCourseName("Math");

        assertEquals(expected, result);
        verify(studentDao).findByCourseName("Math");
    }

    @Test
    void shouldAddStudent() {
        Student student = new Student(0, null, "Jane", "Roe");

        studentService.addStudent(student);

        verify(studentDao).save(student);
    }

    @Test
    void shouldDeleteStudentWhenExists() {
        when(studentDao.findById(5)).thenReturn(Optional.of(new Student(5, new Group(1, "AA-11"), "Max", "Mux")));

        studentService.deleteStudent(5);

        verify(studentDao).deleteById(5);
    }

    @Test
    void shouldThrowWhenDeletingMissingStudent() {
        when(studentDao.findById(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> studentService.deleteStudent(99));

        verify(studentDao, never()).deleteById(anyInt());
    }

    @Test
    void shouldAddStudentToCourse() {
        Student student = new Student(1, new Group(1, "AA-11"), "Max", "Mux");
        Course course = new Course(2, "Math", "Math course");
        when(studentDao.findById(1)).thenReturn(Optional.of(student));
        when(courseDao.findById(2)).thenReturn(Optional.of(course));
        when(courseDao.findByStudentId(1)).thenReturn(List.of());
        studentService.addStudentToCourse(1, 2);
        assertTrue(student.getCourses().contains(course));
    }



    @Test
    void shouldThrowWhenEnrollingOnAlreadyTakenCourse() {
        when(studentDao.findById(1)).thenReturn(Optional.of(new Student(1, new Group(1, "AA-11"), "Max", "Mux")));
        when(courseDao.findById(2)).thenReturn(Optional.of(new Course(2, "Math", "Math course")));
        when(courseDao.findByStudentId(1)).thenReturn(List.of(new Course(2, "Math", "Math course")));

        assertThrows(IllegalArgumentException.class, () -> studentService.addStudentToCourse(1, 2));

        verify(studentDao, never()).addToCourse(anyInt(), anyInt());
    }

    @Test
    void shouldRemoveStudentFromCourse() {
        when(courseDao.findByStudentId(1)).thenReturn(List.of(new Course(2, "Math", "Math course")));

        studentService.removeStudentFromCourse(1, 2);

        verify(studentDao).removeFromCourse(1, 2);
    }

    @Test
    void shouldThrowWhenRemovingNotEnrolledCourse() {
        when(courseDao.findByStudentId(1)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> studentService.removeStudentFromCourse(1, 2));

        verify(studentDao, never()).removeFromCourse(anyInt(), anyInt());
    }
}

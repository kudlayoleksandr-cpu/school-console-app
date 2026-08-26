package ua.foxminded.school;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ua.foxminded.school.Services.CourseService;
import ua.foxminded.school.Services.GroupService;
import ua.foxminded.school.Services.StudentService;
import ua.foxminded.school.model.Course;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;

import java.util.List;
import java.util.Scanner;

@Component
@Order(2)
public class ConsoleMenu implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ConsoleMenu.class);

    private static final String STUDENT_ID = "Enter student ID: ";
    private static final String COURSE_ID = "Enter course ID: ";

    private final GroupService groupService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(GroupService groupService,
                       StudentService studentService,
                       CourseService courseService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting console menu");
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            if (choice == 0) {
                log.info("Exit selected, closing console menu");
                System.out.println("Bye!");
                break;
            }
            try {
                handleChoice(choice);
            } catch (RuntimeException e) {
                log.error("Menu action {} failed", choice, e);
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Find all groups with less or equal students' number");
        System.out.println("2. Find all students related to the course with the given name");
        System.out.println("3. Add a new student");
        System.out.println("4. Delete a student by STUDENT_ID");
        System.out.println("5. Add a student to a course (from the list)");
        System.out.println("6. Remove a student from one of their courses");
        System.out.println("0. Exit");
    }

    private void handleChoice(int choice) {
        log.info("User chose menu option {}", choice);
        switch (choice) {
            case 1 -> findGroupsByStudentCount();
            case 2 -> findStudentsByCourseName();
            case 3 -> addStudent();
            case 4 -> deleteStudent();
            case 5 -> addStudentToCourse();
            case 6 -> removeStudentFromCourse();
            default -> {
                log.warn("Unknown menu option {}", choice);
                System.out.println("Unknown option: " + choice);
            }
        }
    }

    private void findGroupsByStudentCount() {
        int max = readInt("Enter max students' number: ");
        List<Group> groups = groupService.findGroupsWithLessOrEqualStudents(max);
        if (groups.isEmpty()) {
            System.out.println("No groups found.");
            return;
        }
        groups.forEach(g -> System.out.println(g.getGroupId() + " " + g.getGroupName()));
    }

    private void findStudentsByCourseName() {
        System.out.print("Enter a course name: ");
        String name = scanner.nextLine();
        List<Student> students = studentService.findStudentsByCourseName(name);
        if (students.isEmpty()) {
            System.out.println("No students found for course '" + name + "'.");
            return;
        }
        students.forEach(this::printStudent);
    }

    private void addStudent() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        studentService.addStudent(new Student(null, firstName, lastName));
        System.out.println("Student added.");
    }

    private void deleteStudent() {
        int id = readInt(STUDENT_ID);
        try {
            studentService.deleteStudent(id);
            System.out.println("Student deleted.");
        } catch (IllegalArgumentException e) {
            log.warn("Delete failed: {}", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void addStudentToCourse() {
        int studentId = readInt(STUDENT_ID);
        List<Course> courses = courseService.findAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        System.out.println("Available courses:");
        courses.forEach(this::printCourse);
        int courseId = readInt(COURSE_ID);
        studentService.addStudentToCourse(studentId, courseId);
        System.out.println("Student added to course.");
    }

    private void removeStudentFromCourse() {
        int studentId = readInt(STUDENT_ID);
        List<Course> courses = courseService.findCoursesByStudentId(studentId);
        if (courses.isEmpty()) {
            System.out.println("Student " + studentId + " is not enrolled on any course.");
            return;
        }
        System.out.println("Courses of student " + studentId + ":");
        courses.forEach(this::printCourse);
        int courseId = readInt(COURSE_ID);
        studentService.removeStudentFromCourse(studentId, courseId);
        System.out.println("Student removed from course.");
    }

    private void printStudent(Student s) {
        System.out.println(s.getStudentId() + " " + s.getfirstName() + " " + s.getsecondName());
    }

    private void printCourse(Course c) {
        System.out.println(c.getCourseId() + " " + c.getCourseName());
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.warn("Invalid number entered for prompt '{}'", prompt);
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
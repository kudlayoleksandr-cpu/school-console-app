package ua.foxminded.school;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ua.foxminded.school.Dao.GroupDao;
import ua.foxminded.school.Dao.StudentDao;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;

import java.util.List;
import java.util.Scanner;

@Component
@Order(2)
public class ConsoleMenu implements CommandLineRunner {

    private static final String STUDENT_ID = "Enter student ID: ";
    private static final String COURSE_ID = "Enter course ID: ";

    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(GroupDao groupDao, StudentDao studentDao) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    @Override
    public void run(String... args) {
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            if (choice == 0) {
                System.out.println("Bye!");
                break;
            }
            handleChoice(choice);
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Find all groups with less or equal students' number");
        System.out.println("2. Find all students related to the course with the given name");
        System.out.println("3. Add a new student");
        System.out.println("4. Delete a student by STUDENT_ID");
        System.out.println("5. Assign a student to a group");
        System.out.println("6. Add a student to a course");
        System.out.println("7. Remove a student from a course");
        System.out.println("0. Exit");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> {
                int max = readInt("Enter max students' number: ");
                List<Group> groups = groupDao.findGroupsByStudentCount(max);
                groups.forEach(g -> System.out.println(g.getGroupId() + " " + g.getGroupName()));
            }
            case 2 -> {
                System.out.print("Enter a course name: ");
                String name = scanner.nextLine();
                List<Student> students = studentDao.findByCourseName(name);
                students.forEach(this::printStudent);
            }
            case 3 -> {
                System.out.print("Enter first name: ");
                String firstName = scanner.nextLine();
                System.out.print("Enter last name: ");
                String lastName = scanner.nextLine();
                studentDao.save(new Student(0, null, firstName, lastName));
                System.out.println("Student added.");
            }
            case 4 -> {
                int id = readInt(STUDENT_ID);
                studentDao.deleteById(id);
                System.out.println("Student deleted.");
            }
            case 5 -> {
                int studentId = readInt(STUDENT_ID);
                int groupId = readInt("Enter group ID: ");
                studentDao.assignToGroup(studentId, groupId);
                System.out.println("Student assigned to group.");
            }
            case 6 -> {
                int studentId = readInt(STUDENT_ID);
                int courseId = readInt(COURSE_ID);
                studentDao.addToCourse(studentId, courseId);
                System.out.println("Student added to course.");
            }
            case 7 -> {
                int studentId = readInt(STUDENT_ID);
                int courseId = readInt(COURSE_ID);
                studentDao.removeFromCourse(studentId, courseId);
                System.out.println("Student removed from course.");
            }
            default -> System.out.println("Unknown option: " + choice);
        }
    }

    private void printStudent(Student s) {
        System.out.println(s.getStudentId() + " " + s.getfirstName() + " " + s.getsecondName());
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
package ua.foxminded.school;

import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;
import ua.foxminded.school.Dao.GroupDao;
import ua.foxminded.school.Dao.CourseDao;
import ua.foxminded.school.Dao.StudentDao;



import java.util.List;
import java.util.Scanner;

public class Menu {
    private final GroupDao groupDao = new GroupDao();
    private final CourseDao courseDao = new CourseDao();
    private final StudentDao studentDao = new StudentDao();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) break;
            handleChoice(choice);
        }
    }

    private void printMenu() {
        System.out.println("1.Find all groups with less or equal students’ number");
        System.out.println("2.Find all students related to the course with the given name");
        System.out.println("3.Add a new student");
        System.out.println("4.Delete a student by the STUDENT_ID");
        System.out.println("5.Add a student to the course (from a list)");
        System.out.println("6. Remove the student from one of their courses.");
        System.out.println("0. Exit");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> {
                System.out.println("Enter number");
                int max = Integer.parseInt(scanner.nextLine());
                List<Group> groups = groupDao.findGroupsByStudentCount(max);
                groups.forEach(g -> System.out.println(g.getGroupId() + g.getGroupName()));
            }
            case 2 -> {
                System.out.println("Enter a course name");
                String name = scanner.nextLine();
                List<Student> students = courseDao.findStudentsByCourseName(name);
                students.forEach(s -> System.out.println(s.getStudentId() + " " + s.getfirstName() + " " + s.getsecondName()));
            }
            case 3 -> {
                System.out.println("Enter firstname");
                String firstName = scanner.nextLine();
                System.out.print("Enter lastname: ");
                String lastName = scanner.nextLine();
                studentDao.addStudent(firstName, lastName);

            }
            case 4 -> {
                System.out.println("Enter student_id");
                int id = Integer.parseInt(scanner.nextLine());
                studentDao.deleteStudent(id);

            }
            case 5 -> {
                System.out.print("Enter student ID: ");
                int studentId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter course ID: ");
                int courseId = Integer.parseInt(scanner.nextLine());
                studentDao.addStudentToCourse(studentId, courseId);
            }
            case 6 -> {
                System.out.print("Enter student ID: ");
                int studentId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter course ID: ");
                int courseId = Integer.parseInt(scanner.nextLine());
                studentDao.removeStudentFromCourse(studentId, courseId);
            }


        }
    }
}


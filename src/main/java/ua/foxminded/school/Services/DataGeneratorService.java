package ua.foxminded.school.Services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.school.Dao.CourseDao;
import ua.foxminded.school.Dao.GroupDao;
import ua.foxminded.school.Dao.StudentDao;
import ua.foxminded.school.model.Course;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
@Order(1)
public class DataGeneratorService implements CommandLineRunner {

    private static final String[] FIRST_NAMES = {
            "Andrii", "Anton", "Oleksandr", "Oleksa", "Oleksii",
            "Mark", "Daniil", "Nazar", "Philip", "Lev",
            "Maksim", "Ivan", "Volodymyr", "Bogdan", "Mykhailo",
            "Taras", "Roman", "Dmytro", "Yurii", "Artem"
    };
    private static final String[] LAST_NAMES = {
            "Shevchenko", "Zinchenko", "Kovalenko", "Bondarenko", "Boyko",
            "Trubin", "Kravchenko", "Dovbyk", "Moroz", "Yarmolenko",
            "Mudryk", "Sydorchuk", "Surkis", "Byalskii", "Didyk",
            "Shaparenko", "Kabaev", "Rotan", "Roonie", "Zaxarchenko"
    };
    private static final String[] COURSE_NAMES = {
            "Math", "Biology", "Physics", "Chemistry", "History",
            "Geography", "English", "Basics of Health", "Economics", "Art"
    };

    private static final int GROUPS_COUNT = 10;
    private static final int STUDENTS_COUNT = 200;
    private static final int MIN_GROUP_SIZE = 10;
    private static final int MAX_GROUP_SIZE = 30;
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private final GroupDao groupDao;
    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final Random random = new Random();

    public DataGeneratorService(GroupDao groupDao, CourseDao courseDao, StudentDao studentDao) {
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!groupDao.findAll().isEmpty()) {
            return;
        }
        generateGroups();
        generateCourses();
        generateStudents();
        assignCoursesToStudents();
    }

    private void generateGroups() {
        for (int i = 0; i < GROUPS_COUNT; i++) {
            groupDao.save(new Group(randomGroupName()));
        }
    }

    private void generateCourses() {
        for (String name : COURSE_NAMES) {
            courseDao.save(new Course(name, name + " course"));
        }
    }

    private void generateStudents() {
        List<Group> groups = groupDao.findAll();

        List<Group> assignments = new ArrayList<>(Collections.nCopies(STUDENTS_COUNT, null));
        int index = 0;
        for (Group group : groups) {
            int size = MIN_GROUP_SIZE + random.nextInt(MAX_GROUP_SIZE - MIN_GROUP_SIZE + 1);
            for (int i = 0; i < size && index < STUDENTS_COUNT; i++) {
                assignments.set(index++, group);
            }
        }
        Collections.shuffle(assignments);

        for (int i = 0; i < STUDENTS_COUNT; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            studentDao.save(new Student(assignments.get(i), firstName, lastName));
        }
    }

    private void assignCoursesToStudents() {
        List<Integer> courseIds = new ArrayList<>(
                courseDao.findAll().stream().map(Course::getCourseId).toList());
        for (Student student : studentDao.findAll()) {
            Collections.shuffle(courseIds);
            int count = 1 + random.nextInt(3);
            for (int i = 0; i < count; i++) {
                studentDao.addToCourse(student.getStudentId(), courseIds.get(i));
            }
        }
    }

    private String randomGroupName() {
        char c1 = ALPHABET[random.nextInt(ALPHABET.length)];
        char c2 = ALPHABET[random.nextInt(ALPHABET.length)];
        int number = 10 + random.nextInt(90);
        return "" + c1 + c2 + "-" + number;
    }
}
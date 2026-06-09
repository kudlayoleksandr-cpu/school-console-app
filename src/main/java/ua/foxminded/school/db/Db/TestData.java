package ua.foxminded.school.db.Db;

import ua.foxminded.school.Dao.GroupDao;
import ua.foxminded.school.db.ConnectionManager;
import ua.foxminded.school.Dao.CourseDao;
import ua.foxminded.school.Dao.StudentDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestData {
    private static final String[] firstNames = { "Andrii", "Anton", "Oleksandr", "Oleksa", "Oleksii",
            "Mark", "Daniil", "Nazar", "Philip", "Lev",
            "Maksim", "Ivan", "Volodymyr", "Bogdan", "Mykhailo",
            "Taras", "Roman", "Dmytro", "Yurii", "Artem"
    };
    private static final String[] lastNames={"Shevchenko", "Zinchenko", "Kovalenko", "Bondarenko", "Boyko",
            "Trubin", "Kravchenko", "Dovbyk", "Moroz", "Yarmolenko",
            "Mudryk", "Sydorchuk","Surkis","Byalskii","Didyk","Shaparenko","Kabaev","Rotan","Roonie","Zaxarchenko"
    };
    private static final String[] courseNAMES = {
            "Math", "Biology", "Physics", "Chemistry", "History",
            "Geography", "English", "Basics of Health", "Economics", "Art"
    };
    private final Random random = new Random();
    public void generate() {
        new GroupDao().insertGroups();
        new CourseDao().insertCourses(courseNAMES);
        new StudentDao().insertStudents(firstNames,lastNames);
        new GroupDao().assignStudentsToGroups();
        new StudentDao().assignCoursesToStudents();
    }




}


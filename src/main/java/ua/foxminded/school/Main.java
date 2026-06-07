package ua.foxminded.school;

import ua.foxminded.school.db.ConnectionManager;
import ua.foxminded.school.db.Db.Initializer;
import ua.foxminded.school.db.Db.TestData;


public class Main {
    public static void main(String[] args) {
        new Initializer().run();
        new TestData().generate();
        System.out.println("Data generated successfully");

    }


}


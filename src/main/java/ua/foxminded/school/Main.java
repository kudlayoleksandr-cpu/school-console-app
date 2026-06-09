package ua.foxminded.school;

import ua.foxminded.school.db.ConnectionManager;
import ua.foxminded.school.db.Db.Initializer;
import ua.foxminded.school.db.Db.TestData;
import ua.foxminded.school.Menu;


public class Main {
    public static void main(String[] args) {
        new Initializer().run();
        new TestData().generate();
        new Menu().start();

    }


}


package ua.foxminded.school;

import ua.foxminded.school.db.ConnectionManager;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        ConnectionManager cm = new ConnectionManager();
        try (Connection conn = cm.getConnection()) {
            System.out.println("Connected to DB: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("Connection FAILED: " + e.getMessage());
        }
    }
}


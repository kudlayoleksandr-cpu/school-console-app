package ua.foxminded.school.db.Db;

import ua.foxminded.school.db.ConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Initializer {
    public void run() {
        InputStream is=getClass().getResourceAsStream("/sql/createTables.sql");
        if (is==null){
            throw new RuntimeException("Not found");
        }
        String sql;
        try{
            sql=new String(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute SQL",e);
        }
        try (Connection conn = new ConnectionManager().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute SQL", e);
        }

    }
    }
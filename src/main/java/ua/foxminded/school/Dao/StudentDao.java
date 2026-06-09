package ua.foxminded.school.Dao;

import ua.foxminded.school.db.ConnectionManager;
import ua.foxminded.school.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class StudentDao {
    private final Random random = new Random();
    public void insertStudents(String[] firstNames,String[] lastNames){
        String sql= "INSERT INTO students(first_name,last_name) VALUES (?,?)";
        try (Connection conn= ConnectionManager.getInstance().getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)){
            for (int i=0;i<200;i++){
                ps.setString(1,firstNames[random.nextInt(firstNames.length)]);
                ps.setString(2,lastNames[random.nextInt(lastNames.length)]);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new RuntimeException("Failed to insert students"+e);
        }
    }
    public void assignCoursesToStudents() {
        List<Integer> studentIds = new ArrayList<>();
        try (Connection conn =  ConnectionManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT student_id FROM students")) {
            while (rs.next()) {
                studentIds.add(rs.getInt("student_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get students", e);
        }

        List<Integer> courseIds = new ArrayList<>();
        try (Connection conn =  ConnectionManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT course_id FROM courses")) {
            while (rs.next()) {
                courseIds.add(rs.getInt("course_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get courses", e);
        }


        String sql = "INSERT INTO students_courses(student_id, course_id) VALUES(?, ?)";
        try (Connection conn =  ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int studentId : studentIds) {
                Collections.shuffle(courseIds); // перемішуємо курси
                int count = 1 + random.nextInt(3); // випадково 1, 2 або 3
                for (int i = 0; i < count; i++) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, courseIds.get(i));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign courses", e);
        }
    }
    public void addStudent(String firstName, String lastName){
        String sql="INSERT INTO students(first_name,last_name) VALUES (?,?)";
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add student", e);
        }

    }
}

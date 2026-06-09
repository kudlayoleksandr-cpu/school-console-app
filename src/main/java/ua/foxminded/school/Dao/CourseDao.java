package ua.foxminded.school.Dao;

import ua.foxminded.school.db.ConnectionManager;
import ua.foxminded.school.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CourseDao {
    private final Random random = new Random();

    public void insertCourses(String[] courseNAMES) {
        String sql = "INSERT INTO courses(course_name) VALUES(?)";
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String course : courseNAMES) {
                ps.setString(1, course);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert courses", e);
        }
    }

    public List<Student> findStudentsByCourseName(String courseName) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.student_id, s.group_id, s.first_name, s.last_name " +
                "FROM students s " +
                "JOIN students_courses sc ON s.student_id = sc.student_id " +
                "JOIN courses c ON sc.course_id = c.course_id " +
                "WHERE c.course_name = ?";
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getInt("group_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

}



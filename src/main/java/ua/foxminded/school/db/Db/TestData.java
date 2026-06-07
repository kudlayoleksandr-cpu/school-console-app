package ua.foxminded.school.db.Db;

import ua.foxminded.school.db.ConnectionManager;

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
        insertGroups();
        insertCourses();
        insertStudents();
        assignStudentsToGroups();
        assignCoursesToStudents();
    }

    public void insertGroups(){
    String sql="INSERT INTO groups(group_name) VALUES(?)";
    try(Connection conn=new ConnectionManager().getConnection();
        PreparedStatement ps=conn.prepareStatement(sql)){
        for (int i = 0; i < 10; i++) {
            ps.setString(1, generatorOfGroupNames());
            ps.executeUpdate();
        }
    } catch (
    SQLException e) {
        throw new RuntimeException("Failed to insert groups", e);
    }

}
    private String generatorOfGroupNames(){
        char c1=(char) ('A'+random.nextInt(26));
        char c2=(char) ('A'+random.nextInt(26));
        int n=random.nextInt(99);
        return ""+c1+c2+"-"+n;
    }

    public void insertCourses(){
        String sql="INSERT INTO courses(course_name) VALUES(?)";
        try(Connection conn=new ConnectionManager().getConnection();
             PreparedStatement ps=conn.prepareStatement(sql)){
            for (String course:courseNAMES){
                ps.setString(1,course);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new RuntimeException("Failed to insert courses",e);
        }
    }
    public void insertStudents(){
        String sql= "INSERT INTO students(first_name,last_name) VALUES (?,?)";
        try (Connection conn=new ConnectionManager().getConnection();
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


    public void assignStudentsToGroups() {
        List<Integer> studentIds = new ArrayList<>();
        try (Connection conn = new ConnectionManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT student_id FROM students")) {
            while (rs.next()) {
                studentIds.add(rs.getInt("student_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get students", e);
        }
        Collections.shuffle(studentIds);
        int index = 0;
        String sql = "UPDATE students SET group_id = ? WHERE student_id = ?";
        try (Connection conn = new ConnectionManager().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int groupId = 1; groupId <= 10; groupId++) {
                int count = 10 + random.nextInt(21);
                for (int i = 0; i < count && index < studentIds.size(); i++, index++) {
                    ps.setInt(1, groupId);
                    ps.setInt(2, studentIds.get(index));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign students to groups", e);
        }
    }
    public void assignCoursesToStudents() {
        List<Integer> studentIds = new ArrayList<>();
        try (Connection conn = new ConnectionManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT student_id FROM students")) {
            while (rs.next()) {
                studentIds.add(rs.getInt("student_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get students", e);
        }

        List<Integer> courseIds = new ArrayList<>();
        try (Connection conn = new ConnectionManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT course_id FROM courses")) {
            while (rs.next()) {
                courseIds.add(rs.getInt("course_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get courses", e);
        }


        String sql = "INSERT INTO students_courses(student_id, course_id) VALUES(?, ?)";
        try (Connection conn = new ConnectionManager().getConnection();
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


}


package ua.foxminded.school.Dao;
import ua.foxminded.school.model.Group;
import ua.foxminded.school.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GroupDao {
    private final Random random = new Random();

    private String generatorOfGroupNames() {
        char c1 = (char) ('A' + random.nextInt(26));
        char c2 = (char) ('A' + random.nextInt(26));
        int n = random.nextInt(90) + 10;
        return "" + c1 + c2 + "-" + n;
    }

    public void insertGroups() {
        String sql = "INSERT INTO groups(group_name) VALUES(?)";
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < 10; i++) {
                ps.setString(1, generatorOfGroupNames());
                ps.executeUpdate();
            }
        } catch (
                SQLException e) {
            throw new RuntimeException("Failed to insert groups", e);
        }
    }
    public void assignStudentsToGroups() {
        List<Integer> studentIds = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
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
        try (Connection conn = ConnectionManager.getInstance().getConnection();
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
    public List<Group>findGroupsByStudentCount(int maxCount){
       List<Group> groups=new ArrayList<>();
       String sql= "SELECT g.group_id, g.group_name " +
               "FROM groups g " +
               "LEFT JOIN students s ON g.group_id = s.group_id " +
               "GROUP BY g.group_id, g.group_name " +
               "HAVING COUNT(s.student_id) <=?";
       try (Connection conn=ConnectionManager.getInstance().getConnection();
       PreparedStatement ps=conn.prepareStatement(sql)){
           ps.setInt(1,maxCount);
           ResultSet rs=ps.executeQuery();
           while (rs.next()){
               groups.add(new Group(rs.getInt("group_id"),rs.getString("group_name")));
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return groups;
    }
}

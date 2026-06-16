package ua.foxminded.school.Dao;

import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.school.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getObject("group_id", Integer.class),
                rs.getString("first_name"),
                rs.getString("last_name"));
    }
}
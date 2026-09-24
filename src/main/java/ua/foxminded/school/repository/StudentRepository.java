package ua.foxminded.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.school.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    @Query("select s from Student s join s.courses c where c.courseName = :courseName")
    List<Student> findByCourseName(@Param("courseName") String courseName);
}

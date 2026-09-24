package ua.foxminded.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.school.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCourseName(String courseName);

    @Query("select c from Course c join c.students s where s.studentId = :studentId")
    List<Course> findByStudentId(@Param("studentId") Integer studentId);
}

package ua.foxminded.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.school.model.Group;
import java.util.List;


@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>{
    @Query("select g from StudyGroup g where size(g.students) <= :maxCount")
    List<Group> findGroupsByStudentCount(@Param("maxCount") int maxCount);
}
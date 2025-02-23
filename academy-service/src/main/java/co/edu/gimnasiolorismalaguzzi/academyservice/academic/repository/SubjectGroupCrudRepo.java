package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectGroupCrudRepo extends JpaRepository<SubjectGroup, Integer> {
    @Query(value = """
SELECT
    u.id,
    u.first_name,
    u.last_name
FROM users u
         JOIN group_students gs ON u.id = gs.student_id
         JOIN groups g ON gs.group_id = g.id
         JOIN subject_groups sg ON g.id = sg.group_students
         JOIN subject_professors sp ON sg.subject_professor_id = sp.id
         JOIN subject s ON sp.subject_id = s.id
WHERE s.id = :subjectId
""", nativeQuery = true)
    List<User> findBySubjectId(@Param("subjectId") Integer id);

    List<SubjectGroup> findByGroups_Id(Integer id);

}


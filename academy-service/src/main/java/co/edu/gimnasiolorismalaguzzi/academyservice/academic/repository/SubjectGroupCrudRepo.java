package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
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
/***
    SELECT sub_group.*
    FROM subject_groups sub_group
    JOIN subject_professors sub_prof on sub_prof.id = sub_group.subject_professor_id
    JOIN academic_period ap ON sub_group.academic_period_id = ap.id
    WHERE sub_prof.professor_id = 2
    AND ap.start_date <= TO_DATE('2025-12-31', 'YYYY-MM-DD')
    AND ap.end_date >= TO_DATE( '2025-01-01', 'YYYY-MM-DD')
    AND ap.status IN ('A', 'F');
 **/

    @Query(value = "SELECT sub_group.* FROM subject_groups sub_group " +
            "JOIN subject_professors sub_prof on sub_prof.id = sub_group.subject_professor_id " +
            "JOIN academic_period ap ON sub_group.academic_period_id = ap.id " +
            "WHERE sub_prof.professor_id = :teacherId " +
            "  AND ap.start_date <= TO_DATE(:year || '-12-31', 'YYYY-MM-DD') " +
            "  AND ap.end_date   >= TO_DATE(:year || '-01-01', 'YYYY-MM-DD') " +
            "  AND ap.status IN ('A', 'F')",
            nativeQuery = true)
    List<SubjectGroup> getAllSubjectByTeacher(@Param("teacherId") Integer teacherId,
                                                  @Param("year") Integer year);

    List<SubjectGroup> findByGroups_IdAndSubjectProfessor_Professor_IdAndAcademicPeriod_Id(Integer id, Integer id1, Integer id2);

    List<SubjectGroup> findByGroups_IdAndSubjectProfessor_Subject_IdAndSubjectProfessor_Professor_IdAndAcademicPeriod_Id(Integer id, Integer id1, Integer id2, Integer id3);


    @Query(value = "SELECT sg.* FROM subject_groups sg " +
            "JOIN groups g ON sg.group_students = g.id " +
            "JOIN group_students gs ON g.id = gs.group_id " +
            "JOIN academic_period ap ON sg.academic_period_id = ap.id " +
            "WHERE gs.student_id = :studentId " +
            "AND ap.id IN (SELECT id FROM get_academic_periods(:year))",
            nativeQuery = true)
    List<SubjectGroup> findSubjectGroupsByStudentIdAndAcademicYear(
            @Param("studentId") Integer studentId,
            @Param("year") String year);;

    List<SubjectGroup> findByAcademicPeriod_IdAndGroups_StatusAndGroups_Level_Id(Integer id, String status, Integer id1);

    List<SubjectGroup> findByGroups_IdAndAcademicPeriod_Id(Integer id, Integer id1);
}


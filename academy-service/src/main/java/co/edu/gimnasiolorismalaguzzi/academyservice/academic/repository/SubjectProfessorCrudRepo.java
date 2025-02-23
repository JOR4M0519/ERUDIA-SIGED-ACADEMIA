package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectProfessorCrudRepo extends JpaRepository<SubjectProfessor, Integer> {

    @Query(value = "SELECT * FROM subject_professors WHERE subject_id = :subjectId", nativeQuery = true)
    List<SubjectProfessor> findBySubjectId(@Param("subjectId") Integer subjectId);

    @Query(value = "SELECT DISTINCT sp.* FROM subject_professors sp " +
            "JOIN subject_grade sg ON sp.subject_id = sg.subject_id " +
            "JOIN academic_period ap ON ap.id = sg.period_id " +
            "WHERE sp.professor_id = :teacherId " +
            "  AND ap.start_date <= TO_DATE(:year || '-12-31', 'YYYY-MM-DD') " +
            "  AND ap.end_date   >= TO_DATE(:year || '-01-01', 'YYYY-MM-DD') " +
            "  AND ap.status IN ('A', 'F')",
            nativeQuery = true)
    List<SubjectProfessor> getAllSubjectByTeacher(@Param("teacherId") Integer teacherId,
                                                  @Param("year") Integer year);


    @Override
    Optional<SubjectProfessor> findById(Integer id);
}

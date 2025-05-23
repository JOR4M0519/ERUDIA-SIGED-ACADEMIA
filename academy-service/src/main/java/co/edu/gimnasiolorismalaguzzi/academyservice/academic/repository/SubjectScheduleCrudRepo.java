package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectScheduleCrudRepo extends JpaRepository<SubjectSchedule, Integer> {
    @Transactional
    @Modifying
    @Query("update SubjectSchedule u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    @Override
    Optional<SubjectSchedule> findById(Integer id);

    List<SubjectSchedule> findBySubjectGroup_Groups_Id(Integer id);

    // Llamada al procedimiento almacenado
    @Query(value = "SELECT * FROM get_subject_schedules(:groupId, :periodId, :subjectId, :professorId)",
            nativeQuery = true)
    List<SubjectSchedule> getSubjectSchedules(
            @Param("groupId") Integer groupId,
            @Param("periodId") Integer periodId,
            @Param("subjectId") Integer subjectId,
            @Param("professorId") Integer professorId
    );

}

package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SubjectGradeCrudRepo extends JpaRepository<SubjectGrade, Integer> {
    @Transactional
    @Modifying
    @Query(value = "call recover_student(:idStudent, :idSubject, :idPeriod, :newScore)", nativeQuery = true)
    void recoverStudent(
            @Param("newScore") BigDecimal recoveredGrade,
            @Param("idSubject") Integer subject,
            @Param("idStudent") Integer studentId,
            @Param("idPeriod") Integer periodId
    );

    // Método para el reporte de distribución de notas
    @Query("SELECT sg FROM SubjectGrade sg " +
            "JOIN AcademicPeriod ap ON sg.period.id = ap.id " +
            "WHERE sg.student.id IN :studentIds " +
            "AND sg.subject.id = :subjectId " +
            "AND sg.period.id = :periodId " +
            "AND EXTRACT(YEAR FROM ap.startDate) <= :year " +
            "AND EXTRACT(YEAR FROM ap.endDate) >= :year")
    List<SubjectGrade> findByStudentIdsSubjectPeriodAndYear(
            @Param("studentIds") List<Integer> studentIds,
            @Param("subjectId") Integer subjectId,
            @Param("periodId") Integer periodId,
            @Param("year") Integer year);

    // Método para buscar notas por estudiante, materia y periodo
    @Query("SELECT sg FROM SubjectGrade sg " +
            "WHERE sg.student.id = :studentId " +
            "AND sg.subject.id = :subjectId " +
            "AND sg.period.id = :periodId")
    List<SubjectGrade> findByStudentIdSubjectIdAndPeriodId(
            @Param("studentId") Integer studentId,
            @Param("subjectId") Integer subjectId,
            @Param("periodId") Integer periodId);

    SubjectGrade findByStudent_IdAndSubject_IdAndPeriod_Id(Integer id, Integer id1, Integer id2);

    @Override
    void deleteById(Integer integer);
}

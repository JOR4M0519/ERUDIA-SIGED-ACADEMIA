package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.GradeReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeReportCrudRepo extends JpaRepository<GradeReportView, Long> {

    // Consulta nativa para obtener todos los datos necesarios para el reporte
    @Query(value = "SELECT DISTINCT * FROM v_academic_report " +
            "WHERE group_id = :groupId AND period_id = :periodId",
            nativeQuery = true)
    List<GradeReportView> findByGroupIdAndPeriodId(
            @Param("groupId") Long groupId,
            @Param("periodId") Long periodId);

    // Consulta para un estudiante específico
    @Query(value = "SELECT DISTINCT * FROM v_academic_report " +
            "WHERE group_id = :groupId AND student_id = :studentId AND period_id = :periodId",
            nativeQuery = true)
    List<GradeReportView> findByGroupIdAndStudentIdAndPeriodId(
            @Param("groupId") Long groupId,
            @Param("studentId") Long studentId,
            @Param("periodId") Long periodId);

    // Consulta para un estudiante específico con ordenamiento
    @Query(value = "SELECT DISTINCT * FROM v_academic_report " +
            "WHERE group_id = :groupId AND student_id = :studentId AND period_id = :periodId " +
            "ORDER BY subject_name, knowledge_name",
            nativeQuery = true)
    List<GradeReportView> findByGroupIdAndStudentIdAndPeriodIdOrderBySubjectNameAsc(
            @Param("groupId") Long groupId,
            @Param("studentId") Long studentId,
            @Param("periodId") Long periodId);

    // Consulta para un grupo completo con ordenamiento
    @Query(value = "SELECT DISTINCT * FROM v_academic_report " +
            "WHERE group_id = :groupId AND period_id = :periodId " +
            "ORDER BY student_name, subject_name, knowledge_name",
            nativeQuery = true)
    List<GradeReportView> findByGroupIdAndPeriodIdOrderByStudentNameAscSubjectNameAsc(
            @Param("groupId") Long groupId,
            @Param("periodId") Long periodId);

    // Consulta adicional para obtener información detallada del estudiante
    @Query(value = "SELECT u.id, u.first_name, u.last_name, ud.dni, it.name as document_type, " +
            "g.group_name, g.group_code, ap.name as period_name, ap.year " +
            "FROM users u " +
            "JOIN user_detail ud ON u.id = ud.user_id " +
            "JOIN id_type it ON ud.id_type_id = it.id " +
            "JOIN group_students gs ON u.id = gs.student_id " +
            "JOIN groups g ON gs.group_id = g.id " +
            "JOIN academic_period ap ON :periodId = ap.id " +
            "WHERE u.id = :studentId AND gs.group_id = :groupId AND gs.status = 'A'",
            nativeQuery = true)
    Object[] findStudentDetailsByIdAndGroupId(
            @Param("studentId") Long studentId,
            @Param("groupId") Long groupId,
            @Param("periodId") Long periodId);

    // Consulta para obtener información de los profesores por materia
    @Query(value = "SELECT s.id as subject_id, s.subject_name, u.first_name || ' ' || u.last_name as teacher_name " +
            "FROM subject_groups sg " +
            "JOIN subject s ON sg.subject_id = s.id " +
            "JOIN users u ON sg.subject_professor_id = u.id " +
            "WHERE sg.group_students = :groupId AND sg.academic_period_id = :periodId",
            nativeQuery = true)
    List<Object[]> findTeachersByGroupAndPeriod(
            @Param("groupId") Long groupId,
            @Param("periodId") Long periodId);
}


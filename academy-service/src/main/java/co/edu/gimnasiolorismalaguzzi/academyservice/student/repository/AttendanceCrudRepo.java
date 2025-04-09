package co.edu.gimnasiolorismalaguzzi.academyservice.student.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Attendance;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceCrudRepo extends JpaRepository<Attendance, Integer> {
    @Transactional
    @Modifying
    @Query("update Attendance u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    @Transactional
    @Modifying
    @Query(value = "SELECT a.*" +
            "FROM attendance a \n" +
            "         JOIN public.subject_schedule ss ON a.schedule_id = ss.id\n" +
            "         JOIN public.subject_groups sg ON ss.subject_group_id = sg.id\n" +
            "WHERE sg.group_students = ?1 and sg.subject_professor_id = ?2 and academic_period_id = ?3", nativeQuery = true)
    List<Attendance> getHistoricalAttendance(Integer periodId, Integer subjectId, Integer groupId);

    @Override
    Optional<Attendance> findById(Integer id);

    // Método para verificar asistencias duplicadas
    @Query("SELECT COUNT(a) > 0 FROM Attendance a WHERE a.student.id = ?1 AND a.schedule.id = ?2 AND a.attendanceDate = ?3")
    boolean existsByStudentIdAndScheduleIdAndAttendanceDate(Integer studentId, Integer scheduleId, LocalDate attendanceDate);
}


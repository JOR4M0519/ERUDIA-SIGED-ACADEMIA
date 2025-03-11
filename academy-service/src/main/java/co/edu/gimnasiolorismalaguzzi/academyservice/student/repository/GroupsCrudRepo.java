package co.edu.gimnasiolorismalaguzzi.academyservice.student.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.RepeatingStudentsGroupReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupsCrudRepo extends JpaRepository<Groups, Integer> {
    @Transactional
    @Modifying
    @Query("update Groups u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    @Override
    Optional<Groups> findById(Integer id);

    @Query(value = "SELECT * FROM get_academic_level_report()", nativeQuery = true)
    List<Object[]> getAcademicLevelReport();

    List<Groups> findByStatus(String status);
}

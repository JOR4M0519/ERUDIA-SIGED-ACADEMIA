package co.edu.gimnasiolorismalaguzzi.academyservice.student.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.RepeatingStudentsGroupReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface GroupsCrudRepo extends JpaRepository<Groups, Integer> {
    @Transactional
    @Modifying
    @Query("update Groups u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    @Override
    Optional<Groups> findById(Integer id);

    @Query(value = "SELECT * FROM get_academic_level_report()", nativeQuery = true)
    List<Object[]> getAcademicLevelReport();

    @Query("SELECT g FROM Groups g WHERE CAST(g.level.id AS string) = :levelId AND g.status = :status")
    List<Groups> findByLevelIdAndStatus(@Param("levelId") String levelId, @Param("status") String status);

    // Añadir este método a GroupsCrudRepo.java
    List<Groups> findByLevelIdAndStatus(Integer levelId, String status);

    List<Groups> findByStatus(String status);

    List<Groups> findByLevel_Id(Integer id);
}

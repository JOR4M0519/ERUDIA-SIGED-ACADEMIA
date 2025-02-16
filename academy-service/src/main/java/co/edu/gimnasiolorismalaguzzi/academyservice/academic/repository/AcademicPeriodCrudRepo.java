package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AcademicPeriodCrudRepo extends JpaRepository<AcademicPeriod, Integer> {
    @Transactional
    @Modifying
    @Query("update AcademicPeriod a set a.status = ?1 where a.id = ?2")
    int updateStatusById(String status, Integer id);

    List<AcademicPeriod> findByStatus(String status);
}

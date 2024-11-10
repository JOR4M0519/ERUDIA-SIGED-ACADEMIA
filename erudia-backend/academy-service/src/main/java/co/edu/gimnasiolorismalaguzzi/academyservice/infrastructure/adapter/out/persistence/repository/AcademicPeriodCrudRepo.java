package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.AcademicPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AcademicPeriodCrudRepo extends JpaRepository<AcademicPeriod, Integer> {
    @Transactional
    @Modifying
    @Query("update AcademicPeriod a set a.status = ?1 where a.id = ?2")
    int updateStatusById(String status, Integer id);
}

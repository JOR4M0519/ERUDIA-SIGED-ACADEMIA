package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.StudentTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StudentTrackingCrudRepo extends JpaRepository<StudentTracking, Integer> {
    @Transactional
    @Modifying
    @Query("update StudentTracking u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);
}

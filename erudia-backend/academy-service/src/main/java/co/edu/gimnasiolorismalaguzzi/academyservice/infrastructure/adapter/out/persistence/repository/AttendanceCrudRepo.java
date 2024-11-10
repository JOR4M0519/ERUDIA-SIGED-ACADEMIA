package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Attendance;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.GroupStudent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttendanceCrudRepo extends JpaRepository<Attendance, Integer> {
    @Transactional
    @Modifying
    @Query("update Attendance u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    @Override
    Optional<Attendance> findById(Integer id);
}

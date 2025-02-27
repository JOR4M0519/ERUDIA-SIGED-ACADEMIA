package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.StudentTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentTrackingCrudRepo extends JpaRepository<StudentTracking, Integer> {
    @Transactional
    @Modifying
    @Query("update StudentTracking u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    List<StudentTracking> findByStudent_Id(Integer id);

    List<StudentTracking> findByProfessor_Id(Integer id);
}

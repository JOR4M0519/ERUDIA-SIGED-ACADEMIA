package co.edu.gimnasiolorismalaguzzi.academyservice.student.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupStudentCrudRepo extends JpaRepository<GroupStudent, Integer> {
    @Transactional
    @Modifying
    @Query("update GroupStudent u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Integer id);

    @Override
    Optional<GroupStudent> findById(Integer id);
}

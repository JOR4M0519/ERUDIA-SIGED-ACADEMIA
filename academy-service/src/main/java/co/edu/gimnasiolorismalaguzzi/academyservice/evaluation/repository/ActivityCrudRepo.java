package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ActivityCrudRepo extends JpaRepository<Activity, Integer> {
    @Transactional
    @Modifying
    @Query("update Activity a set a.status = ?1 where a.id = ?2")
    int updateStatusById(String status, Integer id);

    @Override
    Optional<Activity> findById(Integer integer);
}

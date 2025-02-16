package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityGroupCrudRepo extends JpaRepository<ActivityGroup, Integer> {
    List<ActivityGroup> findByGroup_IdAndActivity_StatusNotLike(Integer id, String status);
}

package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityGroupCrudRepo extends JpaRepository<ActivityGroup, Integer> {
}

package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityGroupCrudRepo extends JpaRepository<ActivityGroup, Integer> {
}

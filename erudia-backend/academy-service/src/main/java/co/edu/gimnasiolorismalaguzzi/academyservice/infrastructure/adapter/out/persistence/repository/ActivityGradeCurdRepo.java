package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.ActivityGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityGradeCurdRepo extends JpaRepository<ActivityGrade, Integer> {
}

package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementGroupsCrudRepo extends JpaRepository<AchievementGroup, Integer> {
}

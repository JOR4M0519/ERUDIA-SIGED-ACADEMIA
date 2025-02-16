package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementGroupsCrudRepo extends JpaRepository<AchievementGroup, Integer> {
    AchievementGroup findByGroup_Id(Integer id);

    List<AchievementGroup> findByGroup_IdAndSubjectKnowledge_IdSubject_Id(Integer id, Integer id1);
}

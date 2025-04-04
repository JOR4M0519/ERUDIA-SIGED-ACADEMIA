package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementGroupsCrudRepo extends JpaRepository<AchievementGroup, Integer> {
    List<AchievementGroup> findBySubjectKnowledge_IdSubject_IdAndGroup_IdAndPeriod_Id(Integer id, Integer id1, Integer id2);

    List<AchievementGroup> findByGroup_IdAndPeriod_Id(Integer id, Integer id1);

    List<AchievementGroup> findBySubjectKnowledge_Id(Integer id);

    List<AchievementGroup> findByPeriod_IdAndGroup_Id(Integer id, Integer id1);
}

package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.AchievementGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AchievementGroupsCrudRepo extends JpaRepository<AchievementGroup, Integer> {
    @Query(value = "SELECT ag.* FROM achievement_groups ag " +
            "JOIN subject_knowledge sk ON ag.subject_knowledge_id = sk.id " +
            "JOIN subject s ON sk.id_subject = s.id " +
            "WHERE s.id = :subjectGroupId " +
            "AND ag.group_id = :groupId " +
            "AND ag.period_id = :periodId", nativeQuery = true)
    List<AchievementGroup> findBySubjectKnowledge_IdSubject_IdAndGroup_IdAndPeriod_Id(
            @Param("subjectGroupId") Integer subjectGroupId,
            @Param("groupId") Integer groupId,
            @Param("periodId") Integer periodId);


    //List<AchievementGroup> findBySubjectKnowledge_IdSubject_IdAndGroup_IdAndPeriod_Id(Integer id, Integer id1, Integer id2);


    List<AchievementGroup> findByGroup_IdAndPeriod_Id(Integer id, Integer id1);

    List<AchievementGroup> findBySubjectKnowledge_Id(Integer id);

    List<AchievementGroup> findByPeriod_IdAndGroup_Id(Integer id, Integer id1);
}

package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityGroupCrudRepo extends JpaRepository<ActivityGroup, Integer> {
    List<ActivityGroup> findByGroup_IdAndActivity_StatusNotLike(Integer id, String status);

    //List<ActivityGroup> findByActivity_AchievementGroup_Period_IdAndGroup_Student_IdAndActivity_StatusNotLike(Integer id, Integer id1, String status);

    @Query(value = "SELECT act_g.* FROM activity_group act_g " +
            "JOIN activity act ON act_g.activity_id = act.id " +
            "JOIN achievement_groups ag ON ag.id = act.achievement_groups_id " +
            "JOIN subject_knowledge sk ON ag.subject_knowledge_id = sk.id " +
            "JOIN subject s ON sk.id_subject = s.id " +
            "JOIN subject_groups sg ON sg.academic_period_id = sg.academic_period_id " +
            "JOIN subject_professors sp ON sg.subject_professor_id = sp.id " +
            "JOIN subject s2 ON s2.id = sp.subject_id " +
            "WHERE ag.period_id = :periodId " +
            "AND sg.id = :subjectGroupId " +
            "AND ag.group_id = :groupId " +
            "AND act.status != :status", nativeQuery = true)
    List<ActivityGroup> findActivityGroupsByFilters(
            @Param("periodId") Integer periodId,
            @Param("subjectGroupId") Integer subjectGroupId,
            @Param("groupId") Integer groupId,
            @Param("status") String status);

    //List<ActivityGroup> findByActivity_AchievementGroup_Period_IdAndActivity_AchievementGroup_SubjectKnowledge_IdSubject_IdAndGroup_IdAndActivity_StatusNotLike(Integer id, Integer id1, Integer id2, String status);


    ActivityGroup findFirstByActivity_Id(Integer id);
}

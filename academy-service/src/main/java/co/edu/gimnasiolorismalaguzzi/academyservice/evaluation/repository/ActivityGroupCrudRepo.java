package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityGroupCrudRepo extends JpaRepository<ActivityGroup, Integer> {
    List<ActivityGroup> findByGroup_IdAndActivity_StatusNotLike(Integer id, String status);

    //List<ActivityGroup> findByActivity_AchievementGroup_Period_IdAndGroup_Student_IdAndActivity_StatusNotLike(Integer id, Integer id1, String status);

    @Query(value = "SELECT act_g.* FROM activity_group act_g " +
            "JOIN activity act ON act_g.activity_id = act.id " +
            "JOIN achievement_groups ag ON ag.id = act.achievement_groups_id " +
            "JOIN subject_knowledge sk ON ag.subject_knowledge_id = sk.id " +
            "JOIN subject s ON sk.id_subject = s.id " +
            "JOIN subject_groups sg ON sg.academic_period_id = ag.period_id " +
            "JOIN subject_professors sp ON sg.subject_professor_id = sp.id " +
            "JOIN subject s2 ON s2.id = sp.subject_id " +
            "WHERE ag.period_id = :periodId " +
            "AND sp.id = :subjectProfessorId " +
            "AND ag.group_id = :groupId " +
            "AND ag.group_id = sg.group_students " +
            "AND act.status != :status " +
            "AND s.id = s2.id", nativeQuery = true)
    List<ActivityGroup> findActivityGroupsByFilters(
            @Param("periodId") Integer periodId,
            @Param("subjectProfessorId") Integer subjectProfessorId,
            @Param("groupId") Integer groupId,
            @Param("status") String status);

/*    @Query(value = "SELECT act_g.* FROM activity_group act_g " +
            "JOIN activity act ON act_g.activity_id = act.id " +
            "JOIN achievement_groups ag ON ag.id = act.achievement_groups_id " +
            "JOIN subject_knowledge sk ON ag.subject_knowledge_id = sk.id " +
            "WHERE ag.period_id = :periodId " +
            "AND sk.id_subject = :subjectGroupId " +
            "AND ag.group_id = :groupId " +
            "AND act.status != :status", nativeQuery = true)
    List<ActivityGroup> findActivityBySubjectAndPeriodAndGroupIdAndStatusNotLike(
            @Param("periodId") Integer periodId,
            @Param("subjectGroupId") Integer subjectGroupId,
            @Param("groupId") Integer groupId,
            @Param("status") String status);*/

    @Query(value =
            "SELECT act_g.* " +
                    "  FROM activity_group act_g " +
                    "  JOIN activity          act ON act_g.activity_id = act.id " +
                    "  JOIN achievement_groups ach ON ach.id = act.achievement_groups_id " +
                    "  JOIN subject_knowledge   sk ON sk.id = ach.subject_knowledge_id " +
                    " WHERE ach.period_id    = :periodId       " +  // filtra por periodo
                    "   AND sk.id_subject    = :subjectId      " +  // el verdadero subject_id
                    "   AND act_g.group_id   = :groupId        " +  // ahora filtro en activity_group
                    "   AND act.status       <> :status        ",  // omitimos estado inactivo
            nativeQuery = true
    )
    List<ActivityGroup> findActivityBySubjectAndPeriodAndGroupIdAndStatusNotLike(
            @Param("periodId")  Integer periodId,
            @Param("subjectId") Integer subjectId,   // ojo: aquí pasa el subject_id, no subject_group_id
            @Param("groupId")   Integer groupId,     // el id de la clase/grupo
            @Param("status")    String status
    );

    //List<ActivityGroup> findByActivity_AchievementGroup_Period_IdAndActivity_AchievementGroup_SubjectKnowledge_IdSubject_IdAndGroup_IdAndActivity_StatusNotLike(Integer id, Integer id1, Integer id2, String status);


    ActivityGroup findFirstByActivity_Id(Integer id);

    Optional<ActivityGroup> findByActivity_IdAndGroup_Id(Integer activityId, Integer groupId);
}

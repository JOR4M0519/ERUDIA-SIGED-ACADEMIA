package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityGradeCrudRepo extends JpaRepository<ActivityGrade, Integer> {

    ActivityGrade findByActivity_Id(Integer id);

    ActivityGrade findByActivity_IdAndStudent_Id(Integer id, Integer id1);


    @Query("SELECT act_grd FROM ActivityGrade act_grd " +
            "JOIN act_grd.activity act_grp " +
            "JOIN act_grp.activity act " +
            "JOIN act_grp.group grp_studnt " +
            "WHERE act.id = :activityId " +
            "AND grp_studnt.id = :groupId")
    List<ActivityGrade> findByActivityAndGroupId(@Param("activityId") Integer activityId, @Param("groupId") Integer groupId);

    ActivityGrade findByStudent_IdAndActivity_Activity_Id(Integer id, Integer id1); 

    /*@Query(value = """
        SELECT act_grd.*
        FROM activity_grade act_grd
            JOIN activity act ON act.id = act_grd.activity_id
            JOIN achievement_groups ach_grp ON ach_grp.subject_knowledge_id = act.achievement_groups_id
            JOIN group_students grp_stdn ON grp_stdn.group_id = ach_grp.group_id
            WHERE act_grd.id = ?1
            AND grp_stdn.id = ?2;
    """, nativeQuery = true)
    List<ActivityGrade> findFilteredActivityGrades(Integer activityGradeId, Integer groupStudentId);*/
}

package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityGradeCrudRepo extends JpaRepository<ActivityGrade, Integer> {
    List<ActivityGrade> findByActivity_Activity_AchievementGroup_Period_IdAndStudent_IdAndActivity_Activity_StatusNotLike(
            Integer id,
            Integer id1,
            Object unknownAttr1
    );

    List<ActivityGrade> findByActivity_Activity_AchievementGroup_SubjectKnowledge_IdSubject_IdAndActivity_Activity_AchievementGroup_Period_IdAndStudent_IdAndActivity_Activity_StatusNotLike(
            Integer id,
            Integer id1,
            Integer id2,
            Object unknownAttr1
    );
}

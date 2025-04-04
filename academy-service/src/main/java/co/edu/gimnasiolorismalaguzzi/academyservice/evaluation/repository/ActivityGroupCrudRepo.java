package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityGroupCrudRepo extends JpaRepository<ActivityGroup, Integer> {
    List<ActivityGroup> findByGroup_IdAndActivity_StatusNotLike(Integer id, String status);

    //List<ActivityGroup> findByActivity_AchievementGroup_Period_IdAndGroup_Student_IdAndActivity_StatusNotLike(Integer id, Integer id1, String status);


    List<ActivityGroup> findByActivity_AchievementGroup_Period_IdAndActivity_AchievementGroup_SubjectKnowledge_IdSubject_IdAndGroup_IdAndActivity_StatusNotLike(Integer id, Integer id1, Integer id2, String status);


    ActivityGroup findFirstByActivity_Id(Integer id);
}

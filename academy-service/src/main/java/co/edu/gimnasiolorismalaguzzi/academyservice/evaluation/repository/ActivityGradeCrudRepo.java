package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityGradeCrudRepo extends JpaRepository<ActivityGrade, Integer> {
    List<ActivityGrade> findByActivity_Activity_Subject_IdAndActivity_Activity_Period_IdAndStudent_Id(Integer subjectId, Integer periodId, Integer userId);

    List<ActivityGrade> findByActivity_Activity_Period_IdAndStudent_Id(Integer periodId, Integer userId);

}

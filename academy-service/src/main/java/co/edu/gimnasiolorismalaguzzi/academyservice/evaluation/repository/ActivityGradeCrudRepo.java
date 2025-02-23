package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityGradeCrudRepo extends JpaRepository<ActivityGrade, Integer> {

    ActivityGrade findByActivity_Id(Integer id);

    ActivityGrade findByActivity_IdAndStudent_Id(Integer id, Integer id1);
}

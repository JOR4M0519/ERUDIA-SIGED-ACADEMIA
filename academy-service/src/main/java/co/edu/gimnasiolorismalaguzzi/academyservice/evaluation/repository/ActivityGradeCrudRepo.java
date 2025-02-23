package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityGradeCrudRepo extends JpaRepository<ActivityGrade, Integer> {

    ActivityGrade findByActivity_Id(Integer id);

    Optional<ActivityGrade> findByActivity_IdAndStudent_Id(Integer id, Integer id1);
}

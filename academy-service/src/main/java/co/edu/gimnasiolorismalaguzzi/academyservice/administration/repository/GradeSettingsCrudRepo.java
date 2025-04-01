package co.edu.gimnasiolorismalaguzzi.academyservice.administration.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.GradeSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeSettingsCrudRepo extends JpaRepository<GradeSetting, Integer> {
    List<GradeSetting> findByLevelId(Integer levelId);
}

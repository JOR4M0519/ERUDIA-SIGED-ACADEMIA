package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectGroupCrudRepo extends JpaRepository<SubjectGroup, Integer> {
    List<SubjectGroup> findByGroups_Id(Integer id);
}

package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectKnowledgeCrudRepo extends JpaRepository<SubjectKnowledge, Integer> {

    List<SubjectKnowledge> findByIdSubject_Id(Integer id);
}

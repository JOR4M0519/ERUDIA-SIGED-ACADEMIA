package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.SubjectKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectKnowledgeCrudRepo extends JpaRepository<SubjectKnowledge, Integer> {

}

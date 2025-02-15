package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectProfessorCrudRepo extends JpaRepository<SubjectProfessor, Integer> {

    @Query(value = "SELECT * FROM subject_professors WHERE subject_id = :subjectId", nativeQuery = true)
    List<SubjectProfessor> findBySubjectId(@Param("subjectId") Integer subjectId);

    @Override
    Optional<SubjectProfessor> findById(Integer id);
}

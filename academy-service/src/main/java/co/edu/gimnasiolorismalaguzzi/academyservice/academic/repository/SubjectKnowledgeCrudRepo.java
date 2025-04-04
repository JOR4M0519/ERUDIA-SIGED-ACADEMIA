package co.edu.gimnasiolorismalaguzzi.academyservice.academic.repository;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectKnowledgeCrudRepo extends JpaRepository<SubjectKnowledge, Integer> {

    List<SubjectKnowledge> findByIdSubject_Id(Integer id);

    @Query(value = """
    SELECT sub_know.* FROM subject_knowledge sub_know
    FULL JOIN achievement_groups ach_grp ON sub_know.id = ach_grp.subject_knowledge_id
    WHERE sub_know.id_subject = :subjectId
    AND ach_grp.period_id = :periodId
    """, nativeQuery = true)
    List<SubjectKnowledge> findKnowledgesBySubjectId(@Param("subjectId") Integer subjectId,
                                                     @Param("periodId") Integer periodId);

    List<SubjectKnowledge> findByIdKnowledge_Id(Integer id);
}

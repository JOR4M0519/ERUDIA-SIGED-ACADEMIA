package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceSubjectKnowledgePort extends PersistencePort<SubjectKnowledgeDomain, Integer> {
    List<SubjectKnowledgeDomain> getAllKnowledgesBySubjectIdByPeriodId(Integer subjectId,Integer periodId);
}

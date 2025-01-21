package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;

import java.util.List;

public interface SubjectKnowledgeServicePort {
    List<SubjectKnowledgeDomain> getAllSubject_Knowledge();
    SubjectKnowledgeDomain getSubjectKnowledgeById(Integer id);
    SubjectKnowledgeDomain createSubjectKnowledge(SubjectKnowledgeDomain subjectKnowledgeDomain);
    SubjectKnowledgeDomain updateSubjectKnowledge(Integer id, SubjectKnowledgeDomain subjectKnowledgeDomain);
    void deleteSubjectKnowledge(Integer id);
}

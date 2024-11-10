package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.SubjectKnowledgeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceSubjectKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectKnowledgeDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class SubjectKnowledgeServiceImpl implements SubjectKnowledgeServicePort {

    @Autowired
    private PersistenceSubjectKnowledgePort port;

    @Override
    public List<SubjectKnowledgeDomain> getAllSubject_Knowledge() {
        return port.findAll();
    }

    @Override
    public SubjectKnowledgeDomain getSubjectKnowledgeById(Integer id) {
        return port.findById(id);
    }

    @Override
    public SubjectKnowledgeDomain createSubjectKnowledge(SubjectKnowledgeDomain subjectKnowledgeDomain) {
        return port.save(subjectKnowledgeDomain);
    }

    @Override
    public SubjectKnowledgeDomain updateSubjectKnowledge(Integer id, SubjectKnowledgeDomain subjectKnowledgeDomain) {
        return port.update(id,subjectKnowledgeDomain);
    }

    @Override
    public void deleteSubjectKnowledge(Integer id) {
        port.delete(id);
    }
}

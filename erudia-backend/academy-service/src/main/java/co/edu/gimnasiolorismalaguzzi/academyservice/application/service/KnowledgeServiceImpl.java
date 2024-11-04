package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.KnowledgeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.KnowledgeDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class KnowledgeServiceImpl implements KnowledgeServicePort {

    private final PersistenceKnowledgePort knowledgePort;

    @Autowired
    public KnowledgeServiceImpl(PersistenceKnowledgePort knowledgePort) {
        this.knowledgePort = knowledgePort;
    }


    @Override
    public List<KnowledgeDomain> getAllKnowledge() {
        return knowledgePort.findAll();
    }

    @Override
    public KnowledgeDomain getKnowledgeById(Integer id) {
        return knowledgePort.findById(id);
    }

    @Override
    public KnowledgeDomain createKnowledge(KnowledgeDomain knowledgeDomain) {
        return knowledgePort.save(knowledgeDomain);
    }

    @Override
    public KnowledgeDomain updateKnowledge(Integer id, KnowledgeDomain knowledgeDomain) {
        return knowledgePort.update(id, knowledgeDomain);
    }

    @Override
    public void deleteKnowledge(Integer id) {
        knowledgePort.delete(id);
    }
}

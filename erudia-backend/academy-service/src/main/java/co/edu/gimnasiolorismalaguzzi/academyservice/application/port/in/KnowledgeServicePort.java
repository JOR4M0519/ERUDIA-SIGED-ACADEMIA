package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.KnowledgeDomain;

import java.util.List;

public interface KnowledgeServicePort {
    List<KnowledgeDomain> getAllKnowledge();
    KnowledgeDomain getKnowledgeById(Integer id);
    KnowledgeDomain createKnowledge(KnowledgeDomain knowledgeDomain);
    KnowledgeDomain updateKnowledge(Integer id, KnowledgeDomain knowledgeDomain);
    void deleteKnowledge(Integer id);
}

package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.KnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.PersistenceKnowledgePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/knowledge")
public class KnowledgeController {

    private final PersistenceKnowledgePort knowledgeServicePort;

    public KnowledgeController(PersistenceKnowledgePort knowledgeServicePort) {
        this.knowledgeServicePort = knowledgeServicePort;
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeDomain>> getAllKnowledge(){
        List<KnowledgeDomain> KnowledgeDomains = knowledgeServicePort.findAll();
        return ResponseEntity.ok(KnowledgeDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeDomain> getKnowledgeById(@PathVariable Integer id) {
        KnowledgeDomain Knowledge = knowledgeServicePort.findById(id);
        return ResponseEntity.ok(Knowledge);
    }

    @PostMapping()
    public ResponseEntity<KnowledgeDomain> createKnowledge(@RequestBody KnowledgeDomain KnowledgeDomain) {
        KnowledgeDomain createdKnowledge = knowledgeServicePort.save(KnowledgeDomain);
        return ResponseEntity.ok(createdKnowledge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnowledgeDomain> updateKnowledge(@PathVariable Integer id, @RequestBody KnowledgeDomain KnowledgeDomain) {
        KnowledgeDomain updatedKnowledge = knowledgeServicePort.update(id, KnowledgeDomain);
        return ResponseEntity.ok(updatedKnowledge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Integer id) {
        knowledgeServicePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}

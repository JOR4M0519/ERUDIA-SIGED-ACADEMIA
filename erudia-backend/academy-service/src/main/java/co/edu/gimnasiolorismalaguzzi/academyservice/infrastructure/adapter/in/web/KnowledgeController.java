package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.KnowledgeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.KnowledgeDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/knowledge")
public class KnowledgeController {

    private final KnowledgeServicePort knowledgeServicePort;

    public KnowledgeController(KnowledgeServicePort knowledgeServicePort) {
        this.knowledgeServicePort = knowledgeServicePort;
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeDomain>> getAllKnowledge(){
        List<KnowledgeDomain> KnowledgeDomains = knowledgeServicePort.getAllKnowledge();
        return ResponseEntity.ok(KnowledgeDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeDomain> getKnowledgeById(@PathVariable Integer id) {
        KnowledgeDomain Knowledge = knowledgeServicePort.getKnowledgeById(id);
        return ResponseEntity.ok(Knowledge);
    }

    @PostMapping()
    public ResponseEntity<KnowledgeDomain> createKnowledge(@RequestBody KnowledgeDomain KnowledgeDomain) {
        KnowledgeDomain createdKnowledge = knowledgeServicePort.createKnowledge(KnowledgeDomain);
        return ResponseEntity.ok(createdKnowledge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnowledgeDomain> updateKnowledge(@PathVariable Integer id, @RequestBody KnowledgeDomain KnowledgeDomain) {
        KnowledgeDomain updatedKnowledge = knowledgeServicePort.updateKnowledge(id, KnowledgeDomain);
        return ResponseEntity.ok(updatedKnowledge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Integer id) {
        knowledgeServicePort.deleteKnowledge(id);
        return ResponseEntity.noContent().build();
    }
}

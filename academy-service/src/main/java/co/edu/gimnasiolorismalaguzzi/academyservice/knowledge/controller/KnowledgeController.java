package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.KnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.service.persistence.PersistenceKnowledgePort;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> deleteKnowledge(@PathVariable Integer id) {
        try {
            knowledgeServicePort.delete(id);
            return ResponseEntity.noContent().build();
        } catch (AppException e) {
            if (e.getCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e.getMessage());
            } else if (e.getCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud");
        }
    }
}

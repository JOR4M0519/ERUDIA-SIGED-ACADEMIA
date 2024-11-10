package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.SubjectKnowledgeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectKnowledgeDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subject_knowledge")
public class SubjectKnowledgeController {
    
    private final SubjectKnowledgeServicePort port;

    public SubjectKnowledgeController(SubjectKnowledgeServicePort port) {
        this.port = port;
    }

    @GetMapping
    public ResponseEntity<List<SubjectKnowledgeDomain>> getAllKnowledge(){
        List<SubjectKnowledgeDomain> SubjectKnowledgeDomains = port.getAllSubject_Knowledge();
        return ResponseEntity.ok(SubjectKnowledgeDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectKnowledgeDomain> getKnowledgeById(@PathVariable Integer id) {
        SubjectKnowledgeDomain Knowledge = port.getSubjectKnowledgeById(id);
        return ResponseEntity.ok(Knowledge);
    }

    @PostMapping()
    public ResponseEntity<SubjectKnowledgeDomain> createKnowledge(@RequestBody SubjectKnowledgeDomain SubjectKnowledgeDomain) {
        SubjectKnowledgeDomain createdKnowledge = port.createSubjectKnowledge(SubjectKnowledgeDomain);
        return ResponseEntity.ok(createdKnowledge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectKnowledgeDomain> updateKnowledge(@PathVariable Integer id, @RequestBody SubjectKnowledgeDomain SubjectKnowledgeDomain) {
        SubjectKnowledgeDomain updatedKnowledge = port.updateSubjectKnowledge(id, SubjectKnowledgeDomain);
        return ResponseEntity.ok(updatedKnowledge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Integer id) {
        port.deleteSubjectKnowledge(id);
        return ResponseEntity.noContent().build();
    }
}

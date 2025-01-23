package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.PersistenceSubjectKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subject_knowledge")
public class SubjectKnowledgeController {
    
    private final PersistenceSubjectKnowledgePort subjectKnowledgePort;

    public SubjectKnowledgeController(PersistenceSubjectKnowledgePort subjectKnowledgePort) {
        this.subjectKnowledgePort = subjectKnowledgePort;
    }

    @GetMapping
    public ResponseEntity<List<SubjectKnowledgeDomain>> getAllKnowledge(){
        List<SubjectKnowledgeDomain> SubjectKnowledgeDomains = subjectKnowledgePort.findAll();
        return ResponseEntity.ok(SubjectKnowledgeDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectKnowledgeDomain> getKnowledgeById(@PathVariable Integer id) {
        SubjectKnowledgeDomain Knowledge = subjectKnowledgePort.findById(id);
        return ResponseEntity.ok(Knowledge);
    }

    @PostMapping()
    public ResponseEntity<SubjectKnowledgeDomain> createKnowledge(@RequestBody SubjectKnowledgeDomain SubjectKnowledgeDomain) {
        SubjectKnowledgeDomain createdKnowledge = subjectKnowledgePort.save(SubjectKnowledgeDomain);
        return ResponseEntity.ok(createdKnowledge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectKnowledgeDomain> updateKnowledge(@PathVariable Integer id, @RequestBody SubjectKnowledgeDomain SubjectKnowledgeDomain) {
        SubjectKnowledgeDomain updatedKnowledge = subjectKnowledgePort.update(id, SubjectKnowledgeDomain);
        return ResponseEntity.ok(updatedKnowledge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Integer id) {
        subjectKnowledgePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}

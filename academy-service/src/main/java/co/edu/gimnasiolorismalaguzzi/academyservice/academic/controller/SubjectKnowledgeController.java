package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectKnowledgePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/periods/{periodId}/subjects/{subjectId}")
    public ResponseEntity<?> getAllKnowledgeBySubjectIdByPeriodId(@PathVariable Integer periodId,@PathVariable Integer subjectId){
        List<SubjectKnowledgeDomain> SubjectKnowledgeDomains = subjectKnowledgePort.getAllKnowledgesBySubjectIdByPeriodId(subjectId,periodId);
        return ResponseEntity.ok(SubjectKnowledgeDomains);
    }

    @GetMapping("/subjects/{subjectId}/groups/{groupId}")
    public ResponseEntity<?> getAllKnowledgeBySubjectId(@PathVariable Integer subjectId,@PathVariable Integer groupId){
        List<SubjectKnowledgeDomain> SubjectKnowledgeDomains =
                subjectKnowledgePort.getAllSubjectKnowledgeBySubjectIdAndGroupId(subjectId,groupId);
        return ResponseEntity.ok(SubjectKnowledgeDomains);
    }

    @GetMapping("/subjects/{subjectId}")
    public ResponseEntity<?> getAllKnowledgeBySubjectId(@PathVariable Integer subjectId){
        List<SubjectKnowledgeDomain> SubjectKnowledgeDomains =
                subjectKnowledgePort.getAllSubjectKnowledgeBySubjectId(subjectId);
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
    public ResponseEntity<?> deleteKnowledge(@PathVariable Integer id) {
        try {
            subjectKnowledgePort.delete(id);
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

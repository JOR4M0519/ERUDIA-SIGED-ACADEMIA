package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subjects-groups")
public class SubjectGroupsController {
    private final PersistenceSubjectGroupPort subjectGroupPort;

    public SubjectGroupsController(PersistenceSubjectGroupPort subjectGroupPort) {
        this.subjectGroupPort = subjectGroupPort;
    }

    @GetMapping
    public ResponseEntity<List<SubjectGroupDomain>> getAllSubjectGroups(){
        List<SubjectGroupDomain> subjectGroupDomains = subjectGroupPort.findAll();
        return ResponseEntity.ok(subjectGroupDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectGroupDomain> getSubjectGroupById(@PathVariable Integer id){
        SubjectGroupDomain subjectGroupDomain = subjectGroupPort.findById(id);
        return ResponseEntity.ok(subjectGroupDomain);
    }

    @PostMapping
    public ResponseEntity<SubjectGroupDomain> createSubjectGroup(@RequestBody SubjectGroupDomain subjectGroupDomain){
        SubjectGroupDomain created = subjectGroupPort.save(subjectGroupDomain);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectGroupDomain> updateSubjectGroup(@PathVariable Integer id, @RequestBody SubjectGroupDomain subjectGroupDomain){
        SubjectGroupDomain updated = subjectGroupPort.update(id,subjectGroupDomain);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectGroup(@PathVariable Integer id){
        subjectGroupPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}

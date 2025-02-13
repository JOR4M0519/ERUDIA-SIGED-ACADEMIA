package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subjects/dimensions")
public class SubjectDimensionController{
    private final PersistenceSubjectDimension subjectDimensionPort;

    public SubjectDimensionController(PersistenceSubjectDimension persistenceSubjectDimension) {
        this.subjectDimensionPort = persistenceSubjectDimension;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDimensionDomain>> getAllRelations(){
        List<SubjectDimensionDomain> subjectDimensionDomains = subjectDimensionPort.findAll();
        return ResponseEntity.ok(subjectDimensionDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDimensionDomain> getSubjectDimensionByid(@PathVariable Integer id){
        SubjectDimensionDomain subjectDimensionDomain = subjectDimensionPort.findById(id);
        return ResponseEntity.ok(subjectDimensionDomain);
    }

    @PostMapping
    public ResponseEntity<SubjectDimensionDomain> createSubjectDimension(@RequestBody SubjectDimensionDomain subjectDimensionDomain){
        SubjectDimensionDomain createdSubjectDimension = subjectDimensionPort.save(subjectDimensionDomain);
        return ResponseEntity.ok(createdSubjectDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDimensionDomain> updateSubjectDimension(@PathVariable Integer id, @RequestBody SubjectDimensionDomain subjectDimensionDomain){
        SubjectDimensionDomain updatedSubjectDimension = subjectDimensionPort.update(id, subjectDimensionDomain);
        return ResponseEntity.ok(updatedSubjectDimension);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectDimension(@PathVariable Integer id){
        subjectDimensionPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}

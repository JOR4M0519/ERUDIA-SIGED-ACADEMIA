package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectProfessorAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectDimensionPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subjects-dimensions")
public class SubjectDimensionController {

    private final PersistenceSubjectDimensionPort subjectServicePort;

    public SubjectDimensionController(PersistenceSubjectDimensionPort subjectServicePort) {
        this.subjectServicePort = subjectServicePort;
    }

    @GetMapping()
    public ResponseEntity<List<SubjectDimensionDomain>> getAllSubjects() {
        List<SubjectDimensionDomain> subjects = subjectServicePort.findAll();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDimensionDomain> getSubjectById(@PathVariable Integer id) {
        SubjectDimensionDomain Subject = subjectServicePort.findById(id);
        return ResponseEntity.ok(Subject);
    }

    @PostMapping()
    public ResponseEntity<SubjectDimensionDomain> createSubject(@RequestBody SubjectDimensionDomain SubjectDomain) {
        SubjectDimensionDomain createdSubject = subjectServicePort.save(SubjectDomain);
        return ResponseEntity.ok(createdSubject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDimensionDomain> updateSubject(@PathVariable Integer id, @RequestBody SubjectDimensionDomain SubjectDomain) {
        SubjectDimensionDomain updatedSubject = subjectServicePort.update(id, SubjectDomain);
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Integer id) {
        subjectServicePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}

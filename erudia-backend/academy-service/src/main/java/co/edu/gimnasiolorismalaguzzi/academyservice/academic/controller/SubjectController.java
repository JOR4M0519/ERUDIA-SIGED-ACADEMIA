package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.SubjectServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subjects")
public class SubjectController {

    private final SubjectServicePort subjectServicePort;

    public SubjectController(SubjectServicePort subjectServicePort) {
        this.subjectServicePort = subjectServicePort;
    }

    @GetMapping()
    public ResponseEntity<List<SubjectDomain>> getAllSubjects() {
        List<SubjectDomain> subjects = subjectServicePort.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDomain> getSubjectById(@PathVariable Integer id) {
        SubjectDomain Subject = subjectServicePort.getSubjectById(id);
        return ResponseEntity.ok(Subject);
    }

    @PostMapping()
    public ResponseEntity<SubjectDomain> createSubject(@RequestBody SubjectDomain SubjectDomain) {
        SubjectDomain createdSubject = subjectServicePort.createSubject(SubjectDomain);
        return ResponseEntity.ok(createdSubject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDomain> updateSubject(@PathVariable Integer id, @RequestBody SubjectDomain SubjectDomain) {
        SubjectDomain updatedSubject = subjectServicePort.updateSubject(id, SubjectDomain);
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Integer id) {
        subjectServicePort.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}

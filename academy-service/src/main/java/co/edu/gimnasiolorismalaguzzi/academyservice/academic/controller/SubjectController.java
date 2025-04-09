package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectProfessorAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectProfessorPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subjects")
public class SubjectController {

    private final PersistenceSubjectPort subjectServicePort;
    private final PersistenceSubjectProfessorPort subjectProfessorPort;

    public SubjectController(PersistenceSubjectPort subjectServicePort, SubjectProfessorAdapter subjectProfessorAdapter, PersistenceSubjectProfessorPort subjectProfessorPort) {
        this.subjectServicePort = subjectServicePort;
        this.subjectProfessorPort = subjectProfessorPort;
    }

    @GetMapping()
    public ResponseEntity<List<SubjectDomain>> getAllSubjects() {
        List<SubjectDomain> subjects = subjectServicePort.findAll();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/professors")
    public ResponseEntity<List<SubjectProfessorDomain>> getAllSubjectsProfessors() {
        List<SubjectProfessorDomain> subjectProfessorDomainList = subjectProfessorPort.findAll();
        return ResponseEntity.ok(subjectProfessorDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDomain> getSubjectById(@PathVariable Integer id) {
        SubjectDomain Subject = subjectServicePort.findById(id);
        return ResponseEntity.ok(Subject);
    }

    @PostMapping("/professors")
    public ResponseEntity<SubjectProfessorDomain> createSubjectsProfessors(
            @RequestBody SubjectProfessorDomain subjectProfessorDomain
    ) {
        SubjectProfessorDomain subjectProfessorDomainList = subjectProfessorPort.save(subjectProfessorDomain);
        return ResponseEntity.ok(subjectProfessorDomainList);
    }
    @PostMapping()
    public ResponseEntity<SubjectDomain> createSubject(@RequestBody SubjectDomain SubjectDomain) {
        SubjectDomain createdSubject = subjectServicePort.save(SubjectDomain);
        return ResponseEntity.ok(createdSubject);
    }
    @PutMapping("/professors/{id}")
    public ResponseEntity<SubjectProfessorDomain> updateSubjectsProfessors(
            @PathVariable Integer id,
            @RequestBody SubjectProfessorDomain subjectProfessorDomain
    ) {
        SubjectProfessorDomain subjectProfessorDomainList =  subjectProfessorPort.update(id,subjectProfessorDomain);
        return ResponseEntity.ok(subjectProfessorDomainList);
    }

    @DeleteMapping("/professors/{id}")
    public ResponseEntity<?> deleteSubjectsProfessors(@PathVariable Integer id) {
        try {
            subjectProfessorPort.delete(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDomain> updateSubject(@PathVariable Integer id, @RequestBody SubjectDomain SubjectDomain) {
        SubjectDomain updatedSubject = subjectServicePort.update(id, SubjectDomain);
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Integer id) {
        try {
            subjectServicePort.delete(id);
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

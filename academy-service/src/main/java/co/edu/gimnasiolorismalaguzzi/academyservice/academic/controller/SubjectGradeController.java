package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;


import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subject-grade")
public class SubjectGradeController {
    private final PersistenceSubjectGradePort subjectGradePort;
    public SubjectGradeController(PersistenceSubjectGradePort subjectGradePort) {
        this.subjectGradePort = subjectGradePort;
    }

    @GetMapping
    public ResponseEntity<List<SubjectGradeDomain>> getAllFinalGrades(){
        List<SubjectGradeDomain> subjectGradeDomains = subjectGradePort.findAll();
        return ResponseEntity.ok(subjectGradeDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectGradeDomain> getFinalGradeById(@PathVariable Integer id){
        SubjectGradeDomain subjectGradeDomain = subjectGradePort.findById(id);
        return ResponseEntity.ok(subjectGradeDomain);
    }

    /**
     * Obtiene la nota final de un estudiante, de una materia en un periodo especifico
     * @param subjectId
     * @param periodId
     * @param studentId
     * @return El estudiante, la materia y el periodo
     */
    @GetMapping("/subjects/{subjectId}/periods/{periodId}/users/{studentId}")
    public ResponseEntity<?> getFinalGradeById(@PathVariable Integer subjectId,@PathVariable Integer periodId,@PathVariable Integer studentId){
        List<SubjectGradeDomain> subjectGradeList = subjectGradePort.findBySubjectPeriodStudentId(subjectId,periodId,studentId);
        return ResponseEntity.ok(subjectGradeList);
    }

    @PostMapping("/recover")
    public ResponseEntity<String> recoverStudent(
            @RequestParam int idStudent,
            @RequestParam int idSubject,
            @RequestParam int idPeriod,
            @RequestParam BigDecimal newScore) {

        subjectGradePort.recoverStudent(idStudent,idSubject,idPeriod,newScore);
        return ResponseEntity.ok("Estudiante recuperado correctamente");
    }

    @PostMapping("/create")
    public ResponseEntity<SubjectGradeDomain> createFinalGrade(@RequestBody SubjectGradeDomain subjectGradeDomain){
        SubjectGradeDomain createGrade = subjectGradePort.save(subjectGradeDomain);
        return ResponseEntity.ok(createGrade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectGradeDomain> updateGrade(@PathVariable Integer id, @RequestBody SubjectGradeDomain subjectGradeDomain){
        SubjectGradeDomain updatedGrade = subjectGradePort.update(id, subjectGradeDomain);
        return ResponseEntity.ok(updatedGrade);
    }

    //No borrar notas

}

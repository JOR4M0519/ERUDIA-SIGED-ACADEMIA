package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;


import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.PersistenceSubjectGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.SubjectGradeAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.ActivityGroupAdapter;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subject-grade")
public class SubjectGradeController {
    private final PersistenceSubjectGradePort subjectGradePort;
    private final ActivityGroupAdapter activityGroupAdapter;

    public SubjectGradeController(PersistenceSubjectGradePort subjectGradePort, ActivityGroupAdapter activityGroupAdapter) {
        this.subjectGradePort = subjectGradePort;
        this.activityGroupAdapter = activityGroupAdapter;
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

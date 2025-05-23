package co.edu.gimnasiolorismalaguzzi.academyservice.academic.controller;


import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.RecoveryPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence.PersistenceSubjectGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.GradeDistributionDTO;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceGradeReportService;
import jakarta.ws.rs.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/subject-grade")
public class SubjectGradeController {
    private final PersistenceSubjectGradePort subjectGradePort;

    private PersistenceGradeReportService persistenceGradeReportService;


    public SubjectGradeController(PersistenceSubjectGradePort subjectGradePort, PersistenceGradeReportService persistenceGradeReportService) {
        this.subjectGradePort = subjectGradePort;
        this.persistenceGradeReportService = persistenceGradeReportService;
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

    @GetMapping("/subjects/{subjectId}/edu-levels/{levelId}/recovery")
    public ResponseEntity<?> getRecoveryReportGrades(@PathVariable Integer subjectId,
                                                     @PathVariable Integer levelId,
                                                     @RequestParam String year){
        List<RecoveryPeriodDomain> recoveryPeriodDomains = subjectGradePort.findRecoveryListSubjects(subjectId,year,levelId);
        return ResponseEntity.ok(recoveryPeriodDomains);
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

    @PutMapping("/recover/{recoveryId}")
    public ResponseEntity<String> recoverStudentEdit(
            @PathVariable int recoveryId,
            @RequestParam BigDecimal newScore,
            @RequestParam String status) {

        subjectGradePort.editRecoverStudent(recoveryId,newScore, status);
        return ResponseEntity.ok("Se actualizo correctamente");
    }

    @DeleteMapping("/recover/{recoverId}")
    public ResponseEntity<String> recoverStudentDelete(
            @PathVariable int recoverId) {
        subjectGradePort.deleteRecoverStudent(recoverId);
        return ResponseEntity.ok("Se borró el registro exitosamente");
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

    @GetMapping("/report/distribution")
    public ResponseEntity<List<GradeDistributionDTO>> getGradeDistribution(
            @RequestParam Integer year,
            @RequestParam Integer periodId,
            @RequestParam String levelId,
            @RequestParam Integer subjectId) {

        List<GradeDistributionDTO> distribution = persistenceGradeReportService.getGradeDistribution(
                year, periodId, levelId, subjectId);

        return ResponseEntity.ok(distribution);
    }
    //No borrar notas

}

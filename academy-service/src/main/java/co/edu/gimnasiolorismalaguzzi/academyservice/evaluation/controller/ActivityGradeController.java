package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistanceActivityGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/activity-grade")
public class ActivityGradeController {
    private final PersistanceActivityGradePort persistanceActivityGradePort;

    public ActivityGradeController(PersistanceActivityGradePort persistanceActivityGradePort) {
        this.persistanceActivityGradePort = persistanceActivityGradePort;
    }

    @GetMapping
    public ResponseEntity<List<ActivityGradeDomain>> getAllActivity_Group(){
        List<ActivityGradeDomain> ActivityGradeDomainList = persistanceActivityGradePort.findAll();
        return ResponseEntity.ok(ActivityGradeDomainList);
    }

    //Se necesita agregar el pageable - Buscar como se usa
    @GetMapping("/periods/{periodId}/users/{userId} ")
    public ResponseEntity<List<?>> getAllActivity_ByPeriodUser(@PathVariable Integer periodId,
                                                               @PathVariable Integer userId){
        List<ActivityGradeDomain> ActivityGradeDomainList = persistanceActivityGradePort.getAllActivity_ByPeriodUser(periodId, userId);
        return ResponseEntity.ok(ActivityGradeDomainList);
    }

    @GetMapping("/subjects/{subjectId}/periods/{periodId}/users/{userId}")
    public ResponseEntity<List<?>> getAllActivity_ByPeriodByStudentBySubject(@PathVariable Integer subjectId,
                                                                                                @PathVariable Integer periodId,
                                                                                                @PathVariable Integer userId ){
        List<ActivityGradeDomain> ActivityGradeDomainList = persistanceActivityGradePort.getAllActivity_ByPeriod_Student_Subject(subjectId,periodId,userId);
        return ResponseEntity.ok(ActivityGradeDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityGradeDomain> getActivityGroupById(@PathVariable Integer id){
        ActivityGradeDomain ActivityGradeDomain = persistanceActivityGradePort.findById(id);
        return ResponseEntity.ok(ActivityGradeDomain);
    }

    @PostMapping
    public ResponseEntity<ActivityGradeDomain> createActivityGroup(@RequestBody ActivityGradeDomain ActivityGradeDomain){
        ActivityGradeDomain createdDimension = persistanceActivityGradePort.save(ActivityGradeDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityGradeDomain> updateActivityGroup(@PathVariable Integer id, @RequestBody ActivityGradeDomain ActivityGradeDomain){
        ActivityGradeDomain updatedDimension = persistanceActivityGradePort.update(id,ActivityGradeDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}

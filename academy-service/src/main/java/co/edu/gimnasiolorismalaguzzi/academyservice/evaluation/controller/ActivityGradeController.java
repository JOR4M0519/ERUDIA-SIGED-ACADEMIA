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

    @GetMapping("/{id}")
    public ResponseEntity<ActivityGradeDomain> getActivityGradeById(@PathVariable Integer id){
        ActivityGradeDomain ActivityGradeDomain = persistanceActivityGradePort.findById(id);
        return ResponseEntity.ok(ActivityGradeDomain);
    }

    @GetMapping("/activities/{activityId}/groups/{groupId}")
    public ResponseEntity<?> getGradeByActivityId(@PathVariable Integer activityId,@PathVariable Integer groupId){
        List<ActivityGradeDomain> ActivityGradeDomain = persistanceActivityGradePort.getGradeByActivityIdGroupId(activityId,groupId);
        return ResponseEntity.ok(ActivityGradeDomain);
    }


    @GetMapping("/activities/{activityId}/students/{studentId}")
    public ResponseEntity<?> getGradeByActivityIdByStudentId(@PathVariable Integer activityId,@PathVariable Integer studentId){
        ActivityGradeDomain ActivityGradeDomain = persistanceActivityGradePort.getGradeByActivityIdByStudentId(activityId,studentId);
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

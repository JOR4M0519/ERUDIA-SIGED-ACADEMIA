package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.ActivityGradeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGradeDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/activity-grade")
public class ActivityGradeController {
    private final ActivityGradeServicePort port;

    public ActivityGradeController(ActivityGradeServicePort port) {
        this.port = port;
    }

    @GetMapping
    public ResponseEntity<List<ActivityGradeDomain>> getAllActivity_Group(){
        List<ActivityGradeDomain> ActivityGradeDomainList = port.getAttAcitivitiesWithGrades();
        return ResponseEntity.ok(ActivityGradeDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityGradeDomain> getActivityGroupById(@PathVariable Integer id){
        ActivityGradeDomain ActivityGradeDomain = port.getActivityGradeById(id);
        return ResponseEntity.ok(ActivityGradeDomain);
    }

    @PostMapping
    public ResponseEntity<ActivityGradeDomain> createActivityGroup(@RequestBody ActivityGradeDomain ActivityGradeDomain){
        ActivityGradeDomain createdDimension = port.createActivityGrade(ActivityGradeDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityGradeDomain> updateActivityGroup(@PathVariable Integer id, @RequestBody ActivityGradeDomain ActivityGradeDomain){
        ActivityGradeDomain updatedDimension = port.updateActivityGrade(id,ActivityGradeDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}

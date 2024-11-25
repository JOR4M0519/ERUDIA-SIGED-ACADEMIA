package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.ActivityServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/activities")
public class ActivityController {

    private final ActivityServicePort activityServicePort;

    public ActivityController(ActivityServicePort activityServicePort) {
        this.activityServicePort = activityServicePort;
    }

    @GetMapping()
    public ResponseEntity<List<ActivityDomain>> getAllActivities() {
        List<ActivityDomain> activityDomains = activityServicePort.getAllActivities();
        return ResponseEntity.ok(activityDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDomain> getEducationalLevelById(@PathVariable Integer id) {
        ActivityDomain activityDomain = activityServicePort.getActivityById(id);
        return ResponseEntity.ok(activityDomain);
    }

    @PostMapping()
    public ResponseEntity<ActivityDomain> createEducationalLevel(@RequestBody ActivityDomain ActivityDomain) {
        ActivityDomain createdActivity = activityServicePort.createActivity(ActivityDomain);
        return ResponseEntity.ok(createdActivity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDomain> updateEducationalLevel(@PathVariable Integer id, @RequestBody ActivityDomain ActivityDomain) {
        ActivityDomain updatedActivity = activityServicePort.updateActivity(id, ActivityDomain);
        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationalLevel(@PathVariable Integer id) {
        activityServicePort.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}

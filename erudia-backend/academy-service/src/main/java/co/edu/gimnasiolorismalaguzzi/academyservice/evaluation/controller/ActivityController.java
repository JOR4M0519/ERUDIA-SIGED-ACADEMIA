package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.PersistenceActivityPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/activities")
public class ActivityController {

    private final PersistenceActivityPort persistenceActivityPort;

    public ActivityController(PersistenceActivityPort persistenceActivityPort) {
        this.persistenceActivityPort = persistenceActivityPort;
    }

    @GetMapping()
    public ResponseEntity<List<ActivityDomain>> getAllActivities() {
        List<ActivityDomain> activityDomains = persistenceActivityPort.findAll();
        return ResponseEntity.ok(activityDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDomain> getEducationalLevelById(@PathVariable Integer id) {
        ActivityDomain activityDomain = persistenceActivityPort.findById(id);
        return ResponseEntity.ok(activityDomain);
    }

    @PostMapping()
    public ResponseEntity<ActivityDomain> createEducationalLevel(@RequestBody ActivityDomain ActivityDomain) {
        ActivityDomain createdActivity = persistenceActivityPort.save(ActivityDomain);
        return ResponseEntity.ok(createdActivity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDomain> updateEducationalLevel(@PathVariable Integer id, @RequestBody ActivityDomain ActivityDomain) {
        ActivityDomain updatedActivity = persistenceActivityPort.update(id, ActivityDomain);
        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationalLevel(@PathVariable Integer id) {
        persistenceActivityPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}

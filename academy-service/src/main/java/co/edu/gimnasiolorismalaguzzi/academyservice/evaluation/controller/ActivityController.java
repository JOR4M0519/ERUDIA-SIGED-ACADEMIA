package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.CreateActivityFront;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceActivityPort;
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
    public ResponseEntity<?> getActivityById(@PathVariable Integer id) {
        ActivityDomain activityDomain = persistenceActivityPort.findById(id);
        return ResponseEntity.ok(activityDomain);
    }

    /**
     * Obtiene los knowledges, junto a los achievements por actividad
     * @param id
     * @return
     */

    @GetMapping("/knowledges/{id}")
    public ResponseEntity<List<?>> getAllActivitiesWithKnowledgesAchievements(@PathVariable Integer id) {
        List<ActivityDomain> activityDomains = persistenceActivityPort.getAllActivitiesWithKnowledgesAchievements(id);
        return ResponseEntity.ok(activityDomains);
    }

    @PostMapping()
    public ResponseEntity<ActivityDomain> createActivity(@RequestBody CreateActivityFront ActivityDomainFront) {
        ActivityDomain createdActivity = persistenceActivityPort.createActivityAndGroup(ActivityDomainFront);
        return ResponseEntity.ok(createdActivity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDomain> updateActivity(@PathVariable Integer id, @RequestBody CreateActivityFront ActivityDomainFront) {
        ActivityDomain updatedActivity = persistenceActivityPort.updateActivityAndGroup(id, ActivityDomainFront);
        return ResponseEntity.ok(updatedActivity);
    }

    @PutMapping("/{activityId}/knowledges/{knowledgeId}")
    public ResponseEntity<ActivityDomain> updateActivityKnowledgeId(@PathVariable Integer activityId,
                                                                    @PathVariable Integer knowledgeId) {
        ActivityDomain updatedActivity = persistenceActivityPort.updateActivityKnowledgeId(activityId, knowledgeId);
        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Integer id) {
        persistenceActivityPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}

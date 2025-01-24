package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.PersistenceActivityGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/activity-group")
public class ActivityGroupController {
    private final PersistenceActivityGroupPort persistenceActivityGroupPort;

    public ActivityGroupController(PersistenceActivityGroupPort persistenceActivityGroupPort) {
        this.persistenceActivityGroupPort = persistenceActivityGroupPort;
    }
    @GetMapping
    public ResponseEntity<List<ActivityGroupDomain>> getAllActivity_Group(){
        List<ActivityGroupDomain> ActivityGroupDomainList = persistenceActivityGroupPort.findAll();
        return ResponseEntity.ok(ActivityGroupDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityGroupDomain> getActivityGroupById(@PathVariable Integer id){
        ActivityGroupDomain ActivityGroupDomain = persistenceActivityGroupPort.findById(id);
        return ResponseEntity.ok(ActivityGroupDomain);
    }

    @PostMapping
    public ResponseEntity<ActivityGroupDomain> createActivityGroup(@RequestBody ActivityGroupDomain activityGroupDomain){
        ActivityGroupDomain createdDimension = persistenceActivityGroupPort.save(activityGroupDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityGroupDomain> updateActivityGroup(@PathVariable Integer id, @RequestBody ActivityGroupDomain ActivityGroupDomain){
        ActivityGroupDomain updatedDimension = persistenceActivityGroupPort.update(id,ActivityGroupDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}

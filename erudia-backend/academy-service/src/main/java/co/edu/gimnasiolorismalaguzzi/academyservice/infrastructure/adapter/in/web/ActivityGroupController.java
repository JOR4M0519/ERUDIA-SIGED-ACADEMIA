package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.ActivityGroupServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGroupDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/activity-group")
public class ActivityGroupController {
    private final ActivityGroupServicePort port;

    public ActivityGroupController(ActivityGroupServicePort port) {
        this.port = port;
    }
    @GetMapping
    public ResponseEntity<List<ActivityGroupDomain>> getAllActivity_Group(){
        List<ActivityGroupDomain> ActivityGroupDomainList = port.getAllActivity_Group();
        return ResponseEntity.ok(ActivityGroupDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityGroupDomain> getActivityGroupById(@PathVariable Integer id){
        ActivityGroupDomain ActivityGroupDomain = port.getActivityGroupById(id);
        return ResponseEntity.ok(ActivityGroupDomain);
    }

    @PostMapping
    public ResponseEntity<ActivityGroupDomain> createActivityGroup(@RequestBody ActivityGroupDomain activityGroupDomain){
        ActivityGroupDomain createdDimension = port.createActivityGroup(activityGroupDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityGroupDomain> updateActivityGroup(@PathVariable Integer id, @RequestBody ActivityGroupDomain ActivityGroupDomain){
        ActivityGroupDomain updatedDimension = port.updateActivityGroup(id,ActivityGroupDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}

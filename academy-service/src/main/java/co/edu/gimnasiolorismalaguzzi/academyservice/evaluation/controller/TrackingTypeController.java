package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentTrackingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.TrackingTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.TrackingType;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceTrackingTypePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/tracking-types")
public class TrackingTypeController {

    private final PersistenceTrackingTypePort persistenceTrackingTypePort;

    public TrackingTypeController(PersistenceTrackingTypePort persistenceTrackingTypePort) {
        this.persistenceTrackingTypePort = persistenceTrackingTypePort;
    }

    @GetMapping
    public ResponseEntity<List<?>> getAllTrackingTypes(){
        List<TrackingTypeDomain> trackingTypeDomains = persistenceTrackingTypePort.findAll();
        return ResponseEntity.ok(trackingTypeDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackingTypeDomain> getTrackingTypeById(@PathVariable Integer id) {
        TrackingTypeDomain trackingTypeDomain = persistenceTrackingTypePort.findById(id);
        return ResponseEntity.ok(trackingTypeDomain);
    }

    @PostMapping()
    public ResponseEntity<TrackingTypeDomain> createTrackingType(@RequestBody TrackingTypeDomain trackingTypeDomain) {
        TrackingTypeDomain createdTrackingType = persistenceTrackingTypePort.save(trackingTypeDomain);
        return ResponseEntity.ok(createdTrackingType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackingTypeDomain> updateTrackingType(@PathVariable Integer id, @RequestBody TrackingTypeDomain StudentTrackingDomain) {
        TrackingTypeDomain updatedTrackingType = persistenceTrackingTypePort.update(id, StudentTrackingDomain);
        return ResponseEntity.ok(updatedTrackingType);
    }

}

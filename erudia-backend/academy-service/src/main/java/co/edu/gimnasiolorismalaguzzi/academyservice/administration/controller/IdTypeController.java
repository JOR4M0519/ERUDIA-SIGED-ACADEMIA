package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceIdTypePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.IdTypeDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/id-types")
public class IdTypeController {

    private final PersistenceIdTypePort persistenceIdTypePort;

    public IdTypeController(PersistenceIdTypePort persistenceIdTypePort) {
        this.persistenceIdTypePort = persistenceIdTypePort;
    }

    @GetMapping()
    public ResponseEntity<List<IdTypeDomain>> getAllIdTypes() {
        List<IdTypeDomain> EducationalLevels = persistenceIdTypePort.findAll();
        return ResponseEntity.ok(EducationalLevels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdTypeDomain> getIdTypeById(@PathVariable Integer id) {
        IdTypeDomain EducationalLevel = persistenceIdTypePort.findById(id);
        return ResponseEntity.ok(EducationalLevel);
    }

    @PostMapping()
    public ResponseEntity<IdTypeDomain> createIdType(@RequestBody IdTypeDomain IdTypeDomain) {
        IdTypeDomain createdEducationalLevel = persistenceIdTypePort.save(IdTypeDomain);
        return ResponseEntity.ok(createdEducationalLevel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdTypeDomain> updateIdType(@PathVariable Integer id, @RequestBody IdTypeDomain IdTypeDomain) {
        IdTypeDomain updatedEducationalLevel = persistenceIdTypePort.update(id, IdTypeDomain);
        return ResponseEntity.ok(updatedEducationalLevel);
    }

}

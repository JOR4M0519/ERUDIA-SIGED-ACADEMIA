package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.IdTypeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.IdTypeDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/id-types")
public class IdTypeController {

    private final IdTypeServicePort servicePort;

    public IdTypeController(IdTypeServicePort servicePort) {
        this.servicePort = servicePort;
    }

    @GetMapping()
    public ResponseEntity<List<IdTypeDomain>> getAllEducationalLevels() {
        List<IdTypeDomain> EducationalLevels = servicePort.getAllTypes();
        return ResponseEntity.ok(EducationalLevels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdTypeDomain> getEducationalLevelById(@PathVariable Integer id) {
        IdTypeDomain EducationalLevel = servicePort.getTypeById(id);
        return ResponseEntity.ok(EducationalLevel);
    }

    @PostMapping()
    public ResponseEntity<IdTypeDomain> createEducationalLevel(@RequestBody IdTypeDomain IdTypeDomain) {
        IdTypeDomain createdEducationalLevel = servicePort.createType(IdTypeDomain);
        return ResponseEntity.ok(createdEducationalLevel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdTypeDomain> updateEducationalLevel(@PathVariable Integer id, @RequestBody IdTypeDomain IdTypeDomain) {
        IdTypeDomain updatedEducationalLevel = servicePort.updateType(id, IdTypeDomain);
        return ResponseEntity.ok(updatedEducationalLevel);
    }
}

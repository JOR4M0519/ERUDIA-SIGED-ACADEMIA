package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.InstitutionServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.InstitutionDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/institutions")
public class InstitutionController {

    private final InstitutionServicePort InstitutionServicePort;

    public InstitutionController(InstitutionServicePort InstitutionServicePort) {
        this.InstitutionServicePort = InstitutionServicePort;
    }

    @GetMapping()
    public ResponseEntity<List<InstitutionDomain>> getAllInstitutions() {
        List<InstitutionDomain> Institutions = InstitutionServicePort.getAllInstitutions();
        return ResponseEntity.ok(Institutions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDomain> getInstitutionById(@PathVariable Integer id) {
        InstitutionDomain Institution = InstitutionServicePort.getInstitutionById(id);
        return ResponseEntity.ok(Institution);
    }

    @PostMapping()
    public ResponseEntity<InstitutionDomain> createInstitution(@RequestBody InstitutionDomain InstitutionDomain) {
        InstitutionDomain createdInstitution = InstitutionServicePort.createInstitution(InstitutionDomain);
        return ResponseEntity.ok(createdInstitution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDomain> updateInstitution(@PathVariable Integer id, @RequestBody InstitutionDomain InstitutionDomain) {
        InstitutionDomain updatedInstitution = InstitutionServicePort.updateInstitution(id, InstitutionDomain);
        return ResponseEntity.ok(updatedInstitution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable Integer id) {
        InstitutionServicePort.deleteInstitution(id);
        return ResponseEntity.noContent().build();
    }
}
